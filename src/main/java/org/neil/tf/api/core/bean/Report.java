package org.neil.tf.api.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

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

    public Report dealReport(Report report){

        return report;
    }

}
