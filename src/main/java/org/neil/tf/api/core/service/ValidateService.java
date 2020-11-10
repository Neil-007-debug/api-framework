package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;
import org.mvel2.compiler.ExecutableAccessor;
import org.neil.tf.api.core.bean.Variables;
import org.neil.tf.api.core.enums.TestConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidateService {

    @Autowired
    private VariableManageService variableManageService;

    public Boolean validate(HttpResponse httpResponse, JSONArray expressions, Variables variables) {
        if (expressions.isEmpty()) {
            return true;
        }
        Map vars = new HashMap();
        vars.put(TestConstant.TEST_RESPONSE.getName(), httpResponse);
        String body = (String) httpResponse.getBody();
        vars.put(TestConstant.TEST_BODYSTRING.getName(), body);
        try {
            JSONObject jsonObject = JSON.parseObject(body);
            vars.put(TestConstant.TEST_BODYJSONOBJECT.getName(), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Boolean result = null;
        try {
            for (int i = 0; i < expressions.size(); i++) {
                String expression = variableManageService.convertVariable(expressions.getString(i), variables);
                result = MVEL.evalToBoolean(expression, vars);
                if (false==result){
                    break;
                }
            }
        }catch (Exception e){
            result=false;
            e.printStackTrace();
        }
        return result;
    }

    public Boolean judgeEnd(HttpResponse httpResponse, String endCondition) {
        Map vars = new HashMap();
        vars.put(TestConstant.TEST_RESPONSE.getName(), httpResponse);
        String bodyString = (String) httpResponse.getBody();
        vars.put(TestConstant.TEST_BODYSTRING.getName(), bodyString);
        try {
            JSONObject jsonObject = JSON.parseObject(bodyString);
            vars.put(TestConstant.TEST_BODYJSONOBJECT.getName(), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return MVEL.evalToBoolean(endCondition, vars);
    }

    @Test
    public void test() {
        List list = new ArrayList();
        list.isEmpty();
        // 计算
        Map vars = new HashMap();
        vars.put("x", new Integer(5));
        vars.put("y", new Integer(10));
        String json = "{\"a\":\"b\"}";
        JSONObject a = JSON.parseObject(json);
        a.containsKey("a");
        vars.put("json", json);

        vars.put("list", list);
        // 第一种方式
        Boolean flag = (Boolean) MVEL.eval("", vars);
        System.out.println("intResult=");
        // 第二种方式
        ExecutableAccessor compiled = (ExecutableAccessor) MVEL.compileExpression("x * y");
//        intResult = (Integer) MVEL.executeExpression(compiled, vars);
//        System.out.println("intResult=" + intResult);
    }
}
