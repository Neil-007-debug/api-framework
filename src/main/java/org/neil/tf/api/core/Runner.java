package org.neil.tf.api.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import lombok.Setter;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.bean.Report;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.RequestConstant;
import org.neil.tf.api.core.enums.TestConstant;
import org.neil.tf.api.core.service.*;
import org.neil.tf.api.utils.JsonReaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Autowired
    private ValidateService validateService;

    @Getter
    @Setter
    private Job job;

    @Getter
    @Setter
    private Variables variables;

    @Getter
    @Setter
    private Report report;

    public void run(String environmentFile, List caseList) throws NoSuchMethodException, InstantiationException, UnirestException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        report = new Report();
        variables = variableMangeService.initEnvironmentVariables(environmentFile);
        for (int i = 0; i < caseList.size(); i++) {
            String jobFileName = (String) caseList.get(i);
            String jobConfig = JsonReaderUtil.readJsonFile(jobFileName);
            report.getTotalLogs().put(jobFileName, execute(jobConfig));
        }
    }

    public JSONArray execute(String jobConfig) throws NoSuchMethodException, InstantiationException, UnirestException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        JSONArray jsonArray = new JSONArray();
        job = initService.initJobConfig(jobConfig);
        initService.runInitMethod(job.getInitMethod());
        variables = variableMangeService.initVariable(variables, job);
        String type = job.getType();
        switch (type) {
            case "regression":
                jsonArray = regressionStart(job);
                break;
            case "integration":
                jsonArray = integrationStart(job);
                break;
        }
        return jsonArray;
    }

    public JSONArray regressionStart(Job job) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnirestException {
        JSONArray jobArray = job.getTestsuite();
        for (int i = 0; i < jobArray.size(); i++) {
            JSONObject logDetail = new JSONObject();
            JobDetail jobDetail = new JobDetail(jobArray.getJSONObject(i));
            jobDetail.setName(jobDetail.getName() + "--" + i);
            List requestInformationList = requestGenerateService.regressionGenerate(job, jobDetail);
            for (int j = 0; j < requestInformationList.size(); j++) {
                JSONObject jsonObject = (JSONObject) requestInformationList.get(j);
                requestService.sendRequest(jsonObject, variables, logDetail);
            }
        }
        return null;
    }

    public JSONArray integrationStart(Job job) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnirestException {
        JSONArray logArray = new JSONArray();
        JSONArray jobArray = job.getTestsuite();
        JobDetail jobDetail = new JobDetail(jobArray.getJSONObject(0));
        if (!StringUtils.isEmpty(job.getDataProvider())) {
            List firstRequests = requestGenerateService.providerGenerate(job.getDataProvider());
            for (int i = 0; i < firstRequests.size(); i++) {
                JSONArray logDetailArray = new JSONArray();
                JSONObject jsonObject = (JSONObject) firstRequests.get(i);
                jsonObject = requestGenerateService.addJobDetail(jsonObject, jobDetail);
                JSONObject logDetail = new JSONObject();
                logDetail = requestService.sendRequest(jsonObject, variables, logDetail);
                HttpResponse response = (HttpResponse) logDetail.get(RequestConstant.REQUEST_RESPONSE.getName());
                variables = variableMangeService.extractVariables(variables, response, jobDetail);
                if (validateService.validate(response, jobDetail.getValidate())) {
                    logDetail.put(TestConstant.TEST_RESULT_NAME.getName(), TestConstant.TEST_RESULT_SUCCEEDED.getName());
                } else {
                    logDetail.put(TestConstant.TEST_RESULT_NAME.getName(), TestConstant.TEST_RESULT_FAILED.getName());
                }
                logDetailArray.add(logDetail);
                for (int j = 1; j < jobArray.size(); j++) {
                    logDetail = new JSONObject();
                    jobDetail = new JobDetail(jobArray.getJSONObject(j));
                    logDetail = requestService.sendRequest(jobDetail, variables, logDetail);
                    HttpResponse httpResponse = (HttpResponse) logDetail.get(RequestConstant.REQUEST_RESPONSE.getName());
                    variables = variableMangeService.extractVariables(variables, httpResponse, jobDetail);
                    if (validateService.validate(httpResponse, jobDetail.getValidate())) {
                        logDetail.put(TestConstant.TEST_RESULT_NAME.getName(), TestConstant.TEST_RESULT_SUCCEEDED.getName());
                    } else {
                        logDetail.put(TestConstant.TEST_RESULT_NAME.getName(), TestConstant.TEST_RESULT_FAILED.getName());
                    }
                    logDetailArray.add(logDetail);
                }
                logArray.add(logDetailArray);
            }
        } else {

        }
        return logArray;
    }
}
