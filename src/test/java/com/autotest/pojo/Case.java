package com.autotest.pojo;

/**
 * Case用例工具类
 * @author shkstart
 * @create 2020-01
 * */
public class Case {

    private String Case_ID; //用例ID
    private String API_ID;  //接口ID --> 1:POST , 2:GET
    private String Desc;    //用例描述
    private String Params;  //API请求参数
    private String ExpectedResponseData; //期望响应结果
    private String ActualResponseData; ///实际响应结果
    private String preValidateSql; //接口执行前脚本验证
    private String preValidateResult; // 接口执行前数据库验证结果
    private String afterValidateResult; // 接口执行后数据库验证结果
    private String afterValidateSql; //接口执行后脚本验证

    public String getCase_ID() {
        return Case_ID;
    }

    public String getAPI_ID() {
        return API_ID;
    }

    public String getDesc() {
        return Desc;
    }

    public String getParams() {
        return Params;
    }

    public void setCase_ID(String case_ID) {
        Case_ID = case_ID;
    }

    public void setAPI_ID(String API_ID) {
        this.API_ID = API_ID;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setParams(String params) {
        Params = params;
    }

    public String getActualResponseData() {
        return ActualResponseData;
    }

    public void setActualResponseData(String actualResponseData) {
        ActualResponseData = actualResponseData;
    }

    public Case() {
    }

    public String getExpectedResponseData() {
        return ExpectedResponseData;
    }

    public void setExpectedResponseData(String expectedResponseData) {
        ExpectedResponseData = expectedResponseData;
    }

    public String getPreValidateSql() {
        return preValidateSql;
    }

    public String getPreValidateResult() {
        return preValidateResult;
    }

    public String getAfterValidateResult() {
        return afterValidateResult;
    }

    public String getAfterValidateSql() {
        return afterValidateSql;
    }

    public void setPreValidateSql(String preValidateSql) {
        this.preValidateSql = preValidateSql;
    }

    public void setPreValidateResult(String preValidateResult) {
        this.preValidateResult = preValidateResult;
    }

    public void setAfterValidateResult(String afterValidateResult) {
        this.afterValidateResult = afterValidateResult;
    }

    public Case(String afterValidateSql) {
        this.afterValidateSql = afterValidateSql;
    }

    public void setAfterValidateSql(String afterValidateSql) {
        this.afterValidateSql = afterValidateSql;
    }

    public Case(String case_ID, String API_ID, String desc, String params, String expectedResponseData, String actualResponseData, String preValidateSql, String preValidateResult, String afterValidateResult, String afterValidateSql) {
        Case_ID = case_ID;
        this.API_ID = API_ID;
        Desc = desc;
        Params = params;
        ExpectedResponseData = expectedResponseData;
        ActualResponseData = actualResponseData;
        this.preValidateSql = preValidateSql;
        this.preValidateResult = preValidateResult;
        this.afterValidateResult = afterValidateResult;
        this.afterValidateSql = afterValidateSql;
    }

    @Override
    public String toString() {
        return "Case{" +
                "Case_ID='" + Case_ID + '\'' +
                ", API_ID='" + API_ID + '\'' +
                ", Desc='" + Desc + '\'' +
                ", Params='" + Params + '\'' +
                ", ExpectedResponseData='" + ExpectedResponseData + '\'' +
                ", ActualResponseData='" + ActualResponseData + '\'' +
                ", preValidateSql='" + preValidateSql + '\'' +
                ", preValidateResult='" + preValidateResult + '\'' +
                ", afterValidateResult='" + afterValidateResult + '\'' +
                ", afterValidateSql='" + afterValidateSql + '\'' +
                '}';
    }
}

