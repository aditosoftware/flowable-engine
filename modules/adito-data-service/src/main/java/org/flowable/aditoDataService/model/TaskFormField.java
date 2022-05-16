package org.flowable.aditoDataService.model;

import java.util.List;

public class TaskFormField {
    private String id;
    private String name;
    private String type;
    private Object defaultValue;
    private Boolean relational;
    private List<FormFieldItem> items;

    public Boolean getRelational() {
        return relational;
    }

    public void setRelational(Boolean relational) {
        this.relational = relational;
    }

    public List<FormFieldItem> getItems() {
        return items;
    }

    public void setItems(List<FormFieldItem> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}