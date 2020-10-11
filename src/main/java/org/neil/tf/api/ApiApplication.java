package org.neil.tf.api;

import com.alibaba.fastjson.JSONReader;
import javafx.application.Application;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neil.tf.api.core.Runner;
import org.neil.tf.api.utils.ApplicationContextGetBeanHelper;
import org.neil.tf.api.utils.JsonReaderUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        Runner runner= (Runner) ApplicationContextGetBeanHelper.getBean("Runner");
        String environmentConfigFile="testcase/environment.json";
        String jobConfigFile="testcase/precheck.json";
        runner.run(JsonReaderUtil.readJsonFile(environmentConfigFile),JsonReaderUtil.readJsonFile(jobConfigFile));
    }

}
