package org.neil.tf.api.core.enums;

public enum VariableType {

    VARIABLE_TYPE_STARTVARIABLE("startVariables"),
    VARIABLE_TYPE_PROCESSVARIABLELIST("processVariablesList");


    private String name;

    VariableType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
