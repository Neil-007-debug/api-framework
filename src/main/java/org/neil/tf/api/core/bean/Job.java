package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Job {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String baseUrl;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String dataProvider;

    @Getter
    @Setter
    private JSONObject variables;

    @Getter
    @Setter
    private JSONObject headers;

    @Getter
    @Setter
    private JSONArray initMethod;

    @Getter
    @Setter
    private JSONArray testsuite;

}
