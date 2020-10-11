package org.neil.tf.api;

import org.junit.jupiter.api.Test;
import org.neil.tf.api.utils.JsonReaderUtil;

public class Example {

    @Test
    public void test() {
        String json = JsonReaderUtil.readJsonFile("testcase/createJob.json");
        String environmentConfig = JsonReaderUtil.readJsonFile("testcase/environment.json");
        System.out.println(environmentConfig);
    }
}
