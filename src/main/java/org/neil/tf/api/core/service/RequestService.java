package org.neil.tf.api.core.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    private HttpResponse httpResponse;

    public HttpResponse sendRequest(String type, String url) throws UnirestException {
        switch (type) {
            case "get":
                httpResponse = Unirest.get(url)
                        .asString();
                break;
            case "post":
                httpResponse = Unirest.post(url)
                        .asString();
                break;
            case "delete":
                httpResponse = Unirest.delete(url)
                        .asString();
                break;
            case "put":
                httpResponse = Unirest.put(url)
                        .asString();
                break;
        }

        return httpResponse;
    }
}
