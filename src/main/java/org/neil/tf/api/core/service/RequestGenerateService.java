package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.enums.RequestConstant;
import org.neil.tf.api.utils.ClassUtil;
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

    public List regressionGenerate(Job job, JobDetail jobDetail) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException {
        List list = new ArrayList();
        if (jobDetail.getParams().isEmpty() && !StringUtils.isEmpty(job.getDataProvider())) {
            Class dataProviderClass = Class.forName(job.getDataProvider());
            Object object = dataProviderClass.newInstance();
            Method dataProviderMethod = dataProviderClass.getMethod("test");
        } else {
            JSONObject requestInformation = new JSONObject();
        }


        return list;
    }

    public List providerGenerate(String provider) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        List list = (List) ClassUtil.runMethodByName(provider);
        return list;
    }

    public List intregrationGenerate(Job job, JobDetail jobDetail) {
        List list = new ArrayList();
        return list;
    }

    public JSONObject addJobDetail(JSONObject jsonObject, JobDetail jobDetail) {
        if (!StringUtils.isEmpty(jobDetail.getUrl())) {
            jsonObject.put(RequestConstant.REQUEST_URL.getName(), jobDetail.getUrl());
        }
        if (!jobDetail.getParams().isEmpty()) {
            jsonObject.put(RequestConstant.REQUEST_PARAMS.getName(), jobDetail.getParams());
        }
        if (!StringUtils.isEmpty(jobDetail.getHeaders())) {
            jsonObject.put(RequestConstant.REQUEST_HEADERS.getName(), jobDetail.getHeaders());
        }
        if (!StringUtils.isEmpty(jobDetail.getMethod())) {
            jsonObject.put(RequestConstant.REQUEST_METHOD.getName(), jobDetail.getMethod());
        }
        if (!StringUtils.isEmpty(jobDetail.getType())) {
            jsonObject.put(RequestConstant.REQUEST_TYPE.getName(), jobDetail.getType());
        }
        if (!StringUtils.isEmpty(jobDetail.getLoopConfig())) {
            jsonObject.put(RequestConstant.REQUEST_LOOPCONFIG.getName(), jobDetail.getLoopConfig());
        }
        return jsonObject;
    }
}
