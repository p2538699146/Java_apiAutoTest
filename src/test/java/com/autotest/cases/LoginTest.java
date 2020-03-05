package com.autotest.cases;

import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.WriteBackData;
import com.autotest.util.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.autotest.util.ExcelUtil.*;

/**
 * @author shkstart
 * @create 2020-01-08-23:44
 */
public class LoginTest extends BaseTest {


    /**
     * 登录接口测试
     * @param Case_ID
     * @param API_ID
     * @param paramter
     * @param expectedResponseData
     */
    @Test(dataProvider = "login")
    public void test_Login(String Case_ID, String API_ID, String paramter, String expectedResponseData ,
                           String preValidateSql, String afterValidateSql) {

        //执行接口调用前，数据查询
        if (preValidateSql != null && preValidateSql.trim().length() > 0) {
            //接口调用前查询想要验证的字段
            String preValidateResult = DBCheckUtil.doQuery(preValidateSql);
            ExcelUtil.writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID, EXCEL_DATA_PREVALIDATERESULT, preValidateResult));
        }
        //根据API_ID获取接口地址
        String url = RestUtil.getUrlByApiId(API_ID);
        //根据API_ID获取接口类型
        String type = RestUtil.getTypeByApiId(API_ID);
        //替换测试数据中的所有变量
        paramter = Global_VariableUtil.replaceVariables(paramter);
        //通过firstJson将参数解析到map中
        Map<String, Object> params = (Map<String, Object>) JSONObject.parse(paramter);
        //调用doService()完成接口调用，拿到接口响应报文
        String result = HttpUtil.doService(type,url,params);
        //调用自定义断言类，如果实际响应结果与预期响应结果一致，返回通过
        result = AssertUtil.assertEquals_Error_code(result, expectedResponseData);
        //保存每次请求的result,为回写数据对象
        writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID,
                EXCEL_WRITE_RESULT_CELLNAME, result));
        //执行接口调用后，数据验证
        if (afterValidateSql != null && afterValidateSql.trim().length() > 0) {
            //接口调用后查询想要验证的数据
            String afterValidateResult = DBCheckUtil.doQuery(afterValidateSql);
            ExcelUtil.writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID,
                    EXCEL_DATA_AFTERVALIDATERESULT, afterValidateResult));
        }
    }


    @DataProvider(name = "login")
    public Object[][] datas_Login() {
        Object[][] body = CaseUtil.getCaseDataByAPI_ID("1",cellName);
        return body;
    }
}
