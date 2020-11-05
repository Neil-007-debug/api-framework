package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

public class Variables {

    @Getter
    @Setter
    private JSONObject environmentVariables;

    @Getter
    @Setter
    private JSONObject processVariables;

    public Variables() {
        environmentVariables=new JSONObject();
        processVariables=new JSONObject();
    }

}
