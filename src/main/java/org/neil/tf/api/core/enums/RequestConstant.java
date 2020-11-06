package org.neil.tf.api.core.enums;

/**
 * @Author: Neil
 * @Description:
 * @CreateDate: 2020/10/15 20:31
 */
public enum RequestConstant {

    REQUEST_URL("url"),
    REQUEST_HEADERS("headers"),
    REQUEST_BODY("body"),
    REQUEST_BASEURL("baseUrl"),
    REQUEST_METHOD("method"),
    REQUEST_PARAMS("params"),
    REQUEST_REQUEST("request"),
    REQUEST_EXTRACT("extract"),
    REQUEST_NAME("name"),
    REQUEST_TYPE("type"),
    REQUEST_VALIDATE("validate"),
    REQUEST_LOOPCONFIG("loop"),
    REQUEST_RESPONSE("response"),
    REQUEST_GET("get"),
    REQUEST_TYPE_SYNC("sync"),
    REQUEST_TYPE_ASYNC("async"),
    REQUEST_INTERVAL("interval"),
    REQUEST_MOSTWAITINGTIME("mostwaitingtime"),
    REQUEST_ENDCONDITION("endcondition");


    private String name;

    RequestConstant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
