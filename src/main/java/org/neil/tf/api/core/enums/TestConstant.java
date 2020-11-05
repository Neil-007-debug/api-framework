package org.neil.tf.api.core.enums;

/**
 * @Author: Neil
 * @Description:
 * @CreateDate: 2020/10/15 21:08
 */
public enum TestConstant {

    TEST_TYPE_REGRESSION("regression"),
    TEST_TYPE_INTEGRATION("integration"),
    TEST_RESULT_NAME("result"),
    TEST_RESULT_SUCCEEDED("succeeded"),
    TEST_RESULT_FAILED("failed"),
    TEST_RESPONSE("response"),
    TEST_BODYSTRING("bodyString"),
    TEST_BODYJSONOBJECT("bodyObject");


    private String name;

    TestConstant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
