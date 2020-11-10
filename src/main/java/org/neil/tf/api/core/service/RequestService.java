package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.awaitility.Awaitility;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.RequestConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.awaitility.Awaitility.await;

@Service
public class RequestService {

    @Autowired
    private VariableManageService variableManageService;

    @Autowired
    private ValidateService validateService;

    private HttpResponse httpResponse;

    @Async
    public Future<JSONObject> sendRequest(JobDetail jobDetail, Variables variables) throws UnirestException, InterruptedException {
        JSONObject logDetail = new JSONObject();
        jobDetail.setValidate(JSON.parseArray(variableManageService.convertVariable(jobDetail.getValidate().toJSONString(),variables)));
        String url = variableManageService.convertVariable(jobDetail.getUrl(), variables);
        String body = variableManageService.convertVariable(jobDetail.getBody(), variables);
        Map header = jobDetail.getHeaders();
        if (null == header) {
            header = new HashMap();
        }
        String method = jobDetail.getMethod();
        String type = jobDetail.getType();
        JSONObject params = jobDetail.getParams();
        if (null != params) {
            params = JSON.parseObject(variableManageService.convertVariable(params.toJSONString(), variables));
            if (RequestConstant.REQUEST_GET.getName().equals(method)) {
                String p = "";
                for (String key : params.keySet()) {
                    p += key + "=" + params.get(key) + "&";
                }
                if (!StringUtils.isEmpty(p)) {
                    url = url + "?" + p.substring(0, p.lastIndexOf("&"));
                }
            }
        }
        if (StringUtils.isEmpty(body) && null != params && !params.isEmpty()) {
            body = params.toJSONString();
        } else {
            body = "";
        }
        if (RequestConstant.REQUEST_TYPE_ASYNC.getName().equals(type)) {
            JSONObject loopConfig = jobDetail.getLoopConfig();
            Long mostTime = loopConfig.getLong(RequestConstant.REQUEST_MOSTWAITINGTIME.getName());
            Long interval = loopConfig.getLong(RequestConstant.REQUEST_INTERVAL.getName());
            String endCondition = loopConfig.getString(RequestConstant.REQUEST_ENDCONDITION.getName());
            httpResponse = send(method, url, header, body);
            String finalUrl = url;
            String finalBody = body;
            Map finalHeader = header;
            await().atMost(mostTime, TimeUnit.MINUTES)
                    .pollInterval(interval, TimeUnit.SECONDS)
                    .until(() -> {
                        httpResponse = send(method, finalUrl, finalHeader, finalBody);
                        return validateService.judgeEnd(httpResponse, endCondition);
                    });
        } else {
            httpResponse = send(method, url, header, body);
        }
        logDetail.put(RequestConstant.REQUEST_URL.getName(), url);
        logDetail.put(RequestConstant.REQUEST_HEADERS.getName(), header);
        logDetail.put(RequestConstant.REQUEST_BODY.getName(), body);
        logDetail.put(RequestConstant.REQUEST_METHOD.getName(), method);
        logDetail.put(RequestConstant.REQUEST_RESPONSE.getName(), httpResponse);
        logDetail.put(RequestConstant.REQUEST_VALIDATE.getName(),jobDetail.getValidate());
        return new AsyncResult<>(logDetail);
    }

    public HttpResponse send(String method, String url, Map header, String body) throws UnirestException {
        switch (method) {
            case "get":
                httpResponse = Unirest.get(url)
                        .headers(header)
                        .asString();
                break;
            case "post":
                httpResponse = Unirest.post(url)
                        .headers(header)
                        .body(body)
                        .asString();
                break;
            case "delete":
                httpResponse = Unirest.delete(url)
                        .headers(header)
                        .body(body)
                        .asString();
                break;
            case "put":
                httpResponse = Unirest.put(url)
                        .headers(header)
                        .body(body)
                        .asString();
                break;
        }
        return httpResponse;
    }
}
