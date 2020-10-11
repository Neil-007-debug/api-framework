package org.neil.tf.api.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.service.InitService;
import org.neil.tf.api.core.service.VariableMangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Runner {

    @Autowired
    private InitService initService;

    @Autowired
    private VariableMangeService variableMangeService;

    private Job job;
    private Variables variables;

    public void run(String environmentConfig, String jobConfig) {
        JSONObject environmentVariable = JSON.parseObject(environmentConfig);
        job = initService.initJobConfig(environmentConfig, jobConfig);
        variables = variableMangeService.initVariable(environmentVariable, job);
        String type = job.getType();
    }
}
