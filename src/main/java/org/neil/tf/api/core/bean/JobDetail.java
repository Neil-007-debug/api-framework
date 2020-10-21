package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neil.tf.api.core.enums.RequestConstant;

@NoArgsConstructor
public class JobDetail {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private JSONObject params;

    @Getter
    @Setter
    private JSONObject headers;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String method;

    @Getter
    @Setter
    private JSONArray validate;

    @Getter
    @Setter
    private JSONObject extract;

    @Getter
    @Setter
    private HttpResponse response;

    @Getter
    @Setter
    private JSONObject loopConfig;

    public JobDetail (JSONObject jsonObject){
        JSONObject request=jsonObject.getJSONObject(RequestConstant.REQUEST_REQUEST.getName());
        setHeaders(request.getJSONObject(RequestConstant.REQUEST_HEADERS.getName()));
        setMethod(request.getString(RequestConstant.REQUEST_METHOD.getName()));
        setParams(request.getJSONObject(RequestConstant.REQUEST_PARAMS.getName()));
        setUrl(request.getString(RequestConstant.REQUEST_URL.getName()));
        setExtract(jsonObject.getJSONObject(RequestConstant.REQUEST_EXTRACT.getName()));
        setName(jsonObject.getString(RequestConstant.REQUEST_NAME.getName()));
        setType(jsonObject.getString(RequestConstant.REQUEST_TYPE.getName()));
        setValidate(jsonObject.getJSONArray(RequestConstant.REQUEST_VALIDATE.getName()));
        setLoopConfig(jsonObject.getJSONObject(RequestConstant.REQUEST_LOOPCONFIG.getName()));
    }
}
