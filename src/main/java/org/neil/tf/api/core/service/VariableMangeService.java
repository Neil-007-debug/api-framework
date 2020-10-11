package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSONObject;
import org.neil.tf.api.core.bean.Job;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.VariableType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VariableMangeService {

    public Variables initVariable(JSONObject environmentVariables, Job job) {
        Variables variables = new Variables();
        variables.setEnvironmentVariables(environmentVariables);
        JSONObject startVariables = job.getVariables();
        startVariables.put("baseUrl", job.getBaseUrl());
        List processVariablesList = new ArrayList();
        variables.getProcessVariables().put(VariableType.VARIABLE_TYPE_STARTVARIABLE.getName(), startVariables);
        variables.getProcessVariables().put(VariableType.VARIABLE_TYPE_PROCESSVARIABLELIST.getName(), processVariablesList);
        return variables;
    }

    public void addProcessVariables(Variables variables, JSONObject processVariables) {
        Object variablesList = variables.getProcessVariables().get("processVariablesList");
        if (variablesList instanceof List) {
            List processVariablesList = (List) variablesList;
            processVariablesList.add(processVariables);
            variables.getProcessVariables().put("processVariablesList", processVariablesList);
        } else {
            System.out.println("convert to list failed");
        }
    }

}

