package com.autotest.pojo;

/**
 * excel中变量表单
 * @author shkstart
 * @create 2020-01-12-18:57
 */
public class Global_Variable {
    private String name;
    private String value;
    private String remarks;
    private String reflectClass;
    private String reflectMethod;
    private String reflectValue;

    public String getReflectClass() {
        return reflectClass;
    }

    public String getReflectMethod() {
        return reflectMethod;
    }

    public String getReflectValue() {
        return reflectValue;
    }

    public void setReflectClass(String reflectClass) {
        this.reflectClass = reflectClass;
    }

    public void setReflectMethod(String reflectMethod) {
        this.reflectMethod = reflectMethod;
    }

    public void setReflectValue(String reflectValue) {
        this.reflectValue = reflectValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Global_Variable() {
    }

    public Global_Variable(String name, String value, String remarks, String reflectClass, String reflectMethod, String reflectValue) {
        this.name = name;
        this.value = value;
        this.remarks = remarks;
        this.reflectClass = reflectClass;
        this.reflectMethod = reflectMethod;
        this.reflectValue = reflectValue;
    }

    @Override
    public String toString() {
        return "Global_Variable{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", remarks='" + remarks + '\'' +
                ", reflectClass='" + reflectClass + '\'' +
                ", reflectMethod='" + reflectMethod + '\'' +
                ", reflectValue='" + reflectValue + '\'' +
                '}';
    }
}
