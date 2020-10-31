package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.RequestConstant;
import org.neil.tf.api.core.enums.VariableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class VariableMangeService {

    private final static Logger logger = LoggerFactory.getLogger(VariableMangeService.class);

    public Variables initVariable(Variables variables, JSONObject environmentVariables, Job job) {
        if (variables.getEnvironmentVariables().isEmpty()) {
            variables.setEnvironmentVariables(environmentVariables);
        }

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

    public void convertVariable(){

    }

}

