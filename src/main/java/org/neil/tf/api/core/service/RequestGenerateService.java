package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.JobDetail;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Neil
 * @Description:
 * @CreateDate: 2020/10/13 15:57
 */
@Service
public class RequestGenerateService {

    public List generate(Job job, JobDetail jobDetail) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException {
        List list=new ArrayList();
        if (jobDetail.getParams().isEmpty()&&!StringUtils.isEmpty(job.getDataProvider())){
            Class dataProviderClass=Class.forName(job.getDataProvider());
            Object object=dataProviderClass.newInstance();
            Method dataProviderMethod=dataProviderClass.getMethod("test");
        }else {
            JSONObject requestInformation=new JSONObject();
        }


        return list;
    }
}
