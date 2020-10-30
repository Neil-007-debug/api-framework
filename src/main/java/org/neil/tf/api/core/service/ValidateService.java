package org.neil.tf.api.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;
import org.mvel2.compiler.ExecutableAccessor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidateService {

    public Boolean validate(HttpResponse httpResponse,String expression){
        Map vars=new HashMap();
        vars.put("response",httpResponse);
        String body= (String) httpResponse.getBody();
        try {
            JSONObject jsonObject=JSON.parseObject(body);
            vars.put("body",jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
            vars.put("body",body);
        }
        Boolean result=(Boolean) MVEL.eval(expression,vars);
        return result;
    }

    @Test
    public void test() {
        List list=new ArrayList();
        list.isEmpty();
        // 计算
        Map vars = new HashMap();
        vars.put("x", new Integer(5));
        vars.put("y", new Integer(10));
        String json="{\"a\":\"b\"}";
        JSONObject a= JSON.parseObject(json);
        a.containsKey("a");
        vars.put("json",json);

        vars.put("list",list);
        // 第一种方式
        Boolean flag = (Boolean) MVEL.eval("",vars);
        System.out.println("intResult=");
        // 第二种方式
        ExecutableAccessor compiled = (ExecutableAccessor) MVEL.compileExpression("x * y");
//        intResult = (Integer) MVEL.executeExpression(compiled, vars);
//        System.out.println("intResult=" + intResult);
    }
}