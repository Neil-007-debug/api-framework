package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.mashape.unirest.http.HttpResponse;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.JobDetail;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.RequestConstant;
import org.neil.tf.api.core.enums.VariableType;
import org.neil.tf.api.utils.JsonReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VariableManageService {

    private final static Logger logger = LoggerFactory.getLogger(VariableManageService.class);

    public Variables initEnvironmentVariables(String environmentFile) {
        Variables variables = new Variables();
        variables.setEnvironmentVariables(JSON.parseObject(JsonReaderUtil.readJsonFile(environmentFile)));
        return variables;
    }

    public Variables initVariable(Variables variables, Job job) {

        JSONObject startVariables = job.getVariables();
        if (!StringUtils.isEmpty(job.getBaseUrl())) {
            startVariables.put(RequestConstant.REQUEST_BASEURL.getName(), job.getBaseUrl());
        }

        variables.getProcessVariables().put(VariableType.VARIABLE_TYPE_STARTVARIABLE.getName(), startVariables);
        if (!variables.getProcessVariables().containsKey(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName())) {
            List processVariablesList = new ArrayList();
            variables.getProcessVariables().put(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName(), processVariablesList);
        }
        return variables;
    }

    public void addProcessVariables(Variables variables, JSONObject processVariables) {
        Object variablesList = variables.getProcessVariables().get(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName());
        if (variablesList instanceof List) {
            List processVariablesList = (List) variablesList;
            processVariablesList.add(processVariables);
            variables.getProcessVariables().put(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName(), processVariablesList);
        } else {
            System.out.println("convert to list failed");
        }
    }

    public Object getValueByName(Variables variables, String name) {
        Object object = null;
        if (variables.getProcessVariables().containsKey(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName())) {
            List list = (List) variables.getProcessVariables().get(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName());
            for (Object jsonObject : list) {
                if (((JSONObject) jsonObject).containsKey(name)) {
                    object = ((JSONObject) jsonObject).get(name);
                    return object;
                }
            }
        }
        if (variables.getProcessVariables().containsKey(VariableType.VARIABLE_TYPE_STARTVARIABLE.getName())) {
            if (variables.getProcessVariables().getJSONObject(VariableType.VARIABLE_TYPE_STARTVARIABLE.getName()).containsKey(name)) {
                object = variables.getProcessVariables().getJSONObject(VariableType.VARIABLE_TYPE_STARTVARIABLE.getName()).get(name);
                return object;
            }
        }
        if (variables.getEnvironmentVariables().containsKey(name)) {
            object = variables.getEnvironmentVariables().get(name);
            return object;
        }
        logger.error("could not find value of " + name);
        return object;
    }

    public String convertVariable(String value, Variables variables) {
        String regex = "\\$\\{\\S*?}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String v = matcher.group();
            String realValue = String.valueOf(getValueByName(variables, v.substring(2, v.lastIndexOf("}"))));
            value = value.replace(v, realValue);
        }
        return value;
    }

    public Variables extractVariables(Variables variable, HttpResponse httpResponse, JobDetail jobDetail) {
        JSONObject extract = jobDetail.getExtract();
        String body = (String) httpResponse.getBody();
        JSONObject variables = new JSONObject();
        try {
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(body);
            for (String key : extract.keySet()) {
                if (extract.getString(key).startsWith("$")){
                    Object value = JsonPath.read(document, extract.getString(key));
                    variables.put(key, value);
                }else {
                    variables.put(key,extract.getString(key));
                }

            }
        } catch (JsonPathException e) {
            e.printStackTrace();
        } finally {
            addProcessVariables(variable, variables);
        }
        return variable;
    }

}

