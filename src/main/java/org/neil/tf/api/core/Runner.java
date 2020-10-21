package org.neil.tf.api.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import lombok.Setter;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.service.InitService;
import org.neil.tf.api.core.service.RequestGenerateService;
import org.neil.tf.api.core.service.RequestService;
import org.neil.tf.api.core.service.VariableMangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class Runner {

    @Autowired
    private InitService initService;

    @Autowired
    private VariableMangeService variableMangeService;

    @Autowired
    private RequestGenerateService requestGenerateService;

    @Autowired
    private RequestService requestService;

    @Getter
    @Setter
    private Job job;

    @Getter
    @Setter
    private Variables variables;

    public void run(Variables variables,String environmentConfig, String jobConfig) throws NoSuchMethodException, InstantiationException, UnirestException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        JSONObject environmentVariable = JSON.parseObject(environmentConfig);
        job = initService.initJobConfig(environmentConfig, jobConfig);
        variables = variableMangeService.initVariable(variables,environmentVariable, job);
        String type = job.getType();
        switch (type){
            case "regression":
                regressionStart(job,variables);
                break;
            case "integration":
                break;
        }
    }

    public void regressionStart(Job job,Variables variables) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnirestException {
        JSONArray jobArray=job.getTestsuite();
        for (int i=0;i<jobArray.size();i++){
            JobDetail jobDetail= new JobDetail(jobArray.getJSONObject(i));
            jobDetail.setName(jobDetail.getName()+"--"+i);
            List requestInformationList=requestGenerateService.generate(job,jobDetail);
            for (int j=0;j<requestInformationList.size();j++){
                JSONObject jsonObject= (JSONObject) requestInformationList.get(j);
                requestService.sendRequest(jsonObject);
            }
        }
    }

    public void integrationStart(){

    }
}
