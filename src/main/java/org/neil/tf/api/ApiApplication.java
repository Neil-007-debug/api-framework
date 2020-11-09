package org.neil.tf.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.neil.tf.api.core.Runner;
import org.neil.tf.api.utils.ApplicationContextGetBeanHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBootApplication
@EnableAsync
public class ApiApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SpringApplication.run(ApiApplication.class, args);
        Runner runner = (Runner) ApplicationContextGetBeanHelper.getBean("Runner");
        String environmentConfigFile = "testcase/environment.json";
        List jobList = new ArrayList();
        jobList.add("testcase/precheck.json");
        try {
            runner.run(environmentConfigFile, jobList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
