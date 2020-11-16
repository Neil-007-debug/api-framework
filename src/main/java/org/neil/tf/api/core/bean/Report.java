package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.neil.tf.api.core.enums.TestConstant;

/**
 * @Author: Neil
 * @Description:
 * @CreateDate: 2020/11/1 21:13
 */
public class Report {

    @Getter
    @Setter
    private Integer totalCasesNumber;

    @Getter
    @Setter
    private Integer succeededCasesNumber;

    @Getter
    @Setter
    private Integer failedCasesNumber;

    @Getter
    @Setter
    private JSONObject totalLogs;

    @Getter
    @Setter
    private JSONArray failedCasesLogs;

    @Getter
    @Setter
    private Variables variables;

    public Report() {
        this.totalLogs = new JSONObject();
        this.failedCasesLogs = new JSONArray();
    }

    public void dealReport(){
        int totalNumber=0;
        int failedNumber=0;
        int succeededNumber=0;
        for (String key:totalLogs.keySet()){
            JSONArray jsonArray=totalLogs.getJSONArray(key);
            for (int i=0;i<jsonArray.size();i++){
                Boolean result=true;
                JSONArray logDetailArray=jsonArray.getJSONArray(i);
                for (int j=0;j<logDetailArray.size();j++){
                    JSONObject logDetail=logDetailArray.getJSONObject(j);
                    if (logDetail.getString(TestConstant.TEST_RESULT_NAME.getName()).equals(TestConstant.TEST_RESULT_SUCCEEDED.getName())){
                        continue;
                    }else {
                        result=false;
                        break;
                    }
                }
                if (result.equals(true)){
                    succeededNumber++;
                }else {
                    failedNumber++;
                    failedCasesLogs.add(logDetailArray);
                }
                totalNumber++;
            }
        }
        totalCasesNumber=totalNumber;
        succeededCasesNumber=succeededNumber;
        failedCasesNumber=failedNumber;
    }

    @Override
    public String toString() {
        return "final report:\n"+
                JSON.toJSONString(totalLogs)+ "\n"+
                "total  "+totalCasesNumber+"  cases tested\n"+
                succeededCasesNumber+"  cases succeeded\n"+
                failedCasesNumber+"  cases failed\n"+
                "failed cases logs: \n"+
                JSON.toJSONString(failedCasesLogs);
    }
}
