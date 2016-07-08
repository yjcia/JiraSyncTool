package com.kewill.jira.model;

/**
 * Created by YanJun on 2016/7/5.
 */
public class ActionDefinition {
    private String actionName;
    private String actionMethodName;
    private String actionClassName;
    private String type;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionMethodName() {
        return actionMethodName;
    }

    public void setActionMethodName(String actionMethodName) {
        this.actionMethodName = actionMethodName;
    }

    public String getActionClassName() {
        return actionClassName;
    }

    public void setActionClassName(String actionClassName) {
        this.actionClassName = actionClassName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ActionDefinition{" +
                "actionName='" + actionName + '\'' +
                ", actionMethodName='" + actionMethodName + '\'' +
                ", actionClassName='" + actionClassName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
