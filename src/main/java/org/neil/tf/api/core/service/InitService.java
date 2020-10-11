package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    public Job initJobConfig(String environmentConfig, String jobConfig) {
        Job job = JSONObject.toJavaObject((JSON) JSONObject.parseObject(jobConfig), Job.class);
        return job;
    }
}
