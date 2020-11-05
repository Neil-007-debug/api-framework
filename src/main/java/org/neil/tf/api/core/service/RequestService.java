package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.RequestConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestService {

    @Autowired
    private VariableManageService variableManageService;

    private HttpResponse httpResponse;

    public JSONObject sendRequest(JobDetail jobDetail, Variables variables, JSONObject logDetail) throws UnirestException {
        String url = variableManageService.convertVariable(jobDetail.getUrl(), variables);
        String body = variableManageService.convertVariable(jobDetail.getParams().toJSONString(), variables);
        JSONObject params = JSON.parseObject(body);
        Map header = jobDetail.getHeaders();
        String method = jobDetail.getMethod();
        if (RequestConstant.REQUEST_GET.getName().equals(method)) {
            String p = "";
            for (String key : params.keySet()) {
                p += key + "=" + params.get(key) + "&";
            }
            url = url + "?" + p.substring(0, p.lastIndexOf("&"));
        }
        String type=jobDetail.getType();
        if (RequestConstant.REQUEST_TYPE_ASYNC.getName().equals(type)){
            JSONObject loopConfig=jobDetail.getLoopConfig();
//            String endCondition=loopConfig.getString()
            httpResponse = send(method, url, header, body);
        }
        httpResponse = send(method, url, header, body);
        logDetail.put(RequestConstant.REQUEST_URL.getName(), url);
        logDetail.put(RequestConstant.REQUEST_HEADERS.getName(), header);
        logDetail.put(RequestConstant.REQUEST_BODY.getName(), body);
        logDetail.put(RequestConstant.REQUEST_METHOD.getName(), method);
        logDetail.put(RequestConstant.REQUEST_RESPONSE.getName(), httpResponse);
        return logDetail;
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
