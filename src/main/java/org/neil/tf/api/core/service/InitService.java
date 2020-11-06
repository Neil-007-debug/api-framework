package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
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
            String className=name.substring(0,name.lastIndexOf("."));
            String methodName=name.substring(name.lastIndexOf(".")+1,name.lastIndexOf("("));
            Class initClass=Class.forName(className);
            Object object=initClass.newInstance();
            Method method=initClass.getDeclaredMethod(methodName);
            method.invoke(object);
        }
    }
}
