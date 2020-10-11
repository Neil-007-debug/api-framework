package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class JobDetail {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private JSONObject params;

    @Getter
    @Setter
    private JSONObject headers;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String method;

    @Getter
    @Setter
    private JSONArray validate;

    @Getter
    @Setter
    private JSONObject extract;

    @Getter
    @Setter
    private HttpResponse response;

    @Getter
    @Setter
    private JSONObject loopConfig;
}
