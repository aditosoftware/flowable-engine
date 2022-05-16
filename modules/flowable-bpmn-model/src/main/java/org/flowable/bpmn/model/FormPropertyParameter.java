package org.flowable.bpmn.model;

public class FormPropertyParameter extends BaseElement {

    protected String value;
    protected String variable;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public FormPropertyParameter clone() {
        FormPropertyParameter clone = new FormPropertyParameter();
        clone.setValues(this);
        return clone;
    }

    public void setValues(FormPropertyParameter otherValue) {
        super.setValues(otherValue);
        setValue(otherValue.getValue());
        setVariable(otherValue.getVariable());
    }
}
