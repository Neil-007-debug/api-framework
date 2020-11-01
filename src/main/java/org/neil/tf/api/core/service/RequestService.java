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
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class RequestService {

    @Autowired
    private VariableMangeService variableMangeService;

    private HttpResponse httpResponse;

    public JSONObject sendRequest(JSONObject jsonObject, Variables variables, JSONObject logDetail) throws UnirestException {
        String url = variableMangeService.convertVariable(jsonObject.getString(RequestConstant.REQUEST_URL.getName()), variables);
        String body = variableMangeService.convertVariable(jsonObject.getJSONObject(RequestConstant.REQUEST_PARAMS.getName()).toJSONString(), variables);
        JSONObject params = JSON.parseObject(body);
        Map header = jsonObject.getJSONObject(RequestConstant.REQUEST_HEADERS.getName());
        String method = jsonObject.getString(RequestConstant.REQUEST_METHOD.getName());
        if (RequestConstant.REQUEST_GET.getName().equals(method)) {
            String p = "";
            for (String key : params.keySet()) {
                p += key + "=" + params.get(key) + "&";
            }
            url = url + "?" + p.substring(0, p.lastIndexOf("&"));
        }
        String type=jsonObject.getString(RequestConstant.REQUEST_TYPE.getName());
        if (RequestConstant.REQUEST_TYPE_ASYNC.getName().equals(type)){
            JSONObject loopConfig=jsonObject.getJSONObject(RequestConstant.REQUEST_LOOPCONFIG.getName());
//            String endCondition=loopConfig.getString()
            httpResponse = send(method, url, header, params, body);
        }
        httpResponse = send(method, url, header, params, body);
        logDetail.put(RequestConstant.REQUEST_URL.getName(), url);
        logDetail.put(RequestConstant.REQUEST_HEADERS.getName(), header);
        logDetail.put(RequestConstant.REQUEST_BODY.getName(), body);
        logDetail.put(RequestConstant.REQUEST_METHOD.getName(), method);
        logDetail.put(RequestConstant.REQUEST_RESPONSE.getName(), httpResponse);
        return logDetail;
    }

    public HttpResponse send(String method, String url, Map header, JSONObject params, String body) throws UnirestException {
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

    public JSONObject sendRequest(JobDetail jobDetail, Variables variables, JSONObject logDetail) throws UnirestException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RequestConstant.REQUEST_URL.getName(), jobDetail.getUrl());
        jsonObject.put(RequestConstant.REQUEST_PARAMS.getName(), jobDetail.getParams());
        jsonObject.put(RequestConstant.REQUEST_METHOD.getName(), jobDetail.getMethod());
        jsonObject.put(RequestConstant.REQUEST_HEADERS.getName(), jobDetail.getHeaders());
        return sendRequest(jsonObject, variables, logDetail);
    }
}
