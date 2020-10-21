package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.neil.tf.api.core.enums.RequestConstant;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestService {

    private HttpResponse httpResponse;

    public HttpResponse sendRequest(JSONObject jsonObject) throws UnirestException {
        String url=jsonObject.getString(RequestConstant.REQUEST_URL.getName());
        String body=jsonObject.getString(RequestConstant.REQUEST_BODY.getName());
        Map header=jsonObject.getJSONObject(RequestConstant.REQUEST_HEADERS.getName());
        String method=jsonObject.getString(RequestConstant.REQUEST_METHOD.getName());
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
