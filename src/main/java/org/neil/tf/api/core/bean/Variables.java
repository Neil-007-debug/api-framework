package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Variables {

    @Getter
    @Setter
    private JSONObject environmentVariables;

    @Getter
    @Setter
    private JSONObject processVariables;

}
