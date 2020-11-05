package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.enums.RequestConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Neil
 * @Description:
 * @CreateDate: 2020/11/5 12:21
 */
@Service
public class JobManageService {

    public JobDetail addVariable(JobDetail jobDetail, JSONObject jsonObject) {
        if (jsonObject.containsKey(RequestConstant.REQUEST_NAME.getName())) {
            jobDetail.setName(jsonObject.getString(RequestConstant.REQUEST_NAME.getName()));
        }
        if (jsonObject.containsKey(RequestConstant.REQUEST_URL.getName())) {
            jobDetail.setUrl(jsonObject.getString(RequestConstant.REQUEST_URL.getName()));
        }
        if (jsonObject.containsKey(RequestConstant.REQUEST_PARAMS.getName())) {
            jobDetail.setParams(jsonObject.getJSONObject(RequestConstant.REQUEST_PARAMS.getName()));
        }
        if (jsonObject.containsKey(RequestConstant.REQUEST_HEADERS.getName())) {
            jobDetail.setHeaders(jsonObject.getJSONObject(RequestConstant.REQUEST_HEADERS.getName()));
        }
        if (jsonObject.containsKey(RequestConstant.REQUEST_VALIDATE.getName())) {
            JSONArray jsonArray= jobDetail.getValidate();
            jsonArray.addAll(jsonObject.getJSONArray(RequestConstant.REQUEST_VALIDATE.getName()));
            jobDetail.setValidate(jsonArray);
        }

        if (jsonObject.containsKey(RequestConstant.REQUEST_EXTRACT.getName())) {
            JSONObject object=jobDetail.getExtract();
            object.putAll(jsonObject.getJSONObject(RequestConstant.REQUEST_EXTRACT.getName()));
            jobDetail.setExtract(object);
        }
        return jobDetail;
    }
}
