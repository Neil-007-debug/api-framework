package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.utils.ClassUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class InitService {

    public Job initJobConfig(String jobConfig) {
        Job job = JSONObject.toJavaObject(JSONObject.parseObject(jobConfig), Job.class);
        return job;
    }

    public void runInitMethod(JSONArray jsonArray) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        for (int i=0;i<jsonArray.size();i++){
            String name=jsonArray.getString(i);
            ClassUtil.runMethodByName(name);
        }
    }

    public void test1(String objects){
        System.out.println(JSON.toJSONString(objects));
    }

    @Test
    public void test() throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        JSONArray jsonArray=new JSONArray();
        jsonArray.add("org.neil.tf.api.core.service.InitService.test1(bbb)");
        runInitMethod(jsonArray);
    }
}
