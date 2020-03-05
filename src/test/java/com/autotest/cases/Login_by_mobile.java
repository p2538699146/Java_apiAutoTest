package com.autotest.cases;

import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.WriteBackData;
import com.autotest.util.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;

import static com.autotest.util.ExcelUtil.*;
import static com.autotest.util.RestUtil.*;

public class Login_by_mobile extends BaseTest{

    /**
     *手机号验证登录接口
     * @param Case_ID   用例id
     * @param API_ID    接口id
     * @param paramter  请求参数
     * @param expectedResponseData  预期结果
     */
    @Test(dataProvider = "Login_mobile")
    public void login_mobile_test(String Case_ID, String API_ID, String paramter,
                                  String expectedResponseData) {
        String url = getUrlByApiId(API_ID);
        String type = getTypeByApiId(API_ID);
        //替换用例中的变量
        //paramter = Global_VariableUtil.replaceJSONAll(paramter);
        Map<String,Object> params = JSONObject.parseObject(paramter);
        String result = HttpUtil.doService(type, url, params);
        result = AssertUtil.assertEquals_Error_code(result,expectedResponseData);
        writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID,
                EXCEL_WRITE_RESULT_CELLNAME,result));
    }

    @DataProvider(name = "Login_mobile")
    public Object[][] Login_mobile_test() {
        Object[][] data = CaseUtil.getCaseDataByAPI_ID("5", cellName_1);
        return data;
    }
}
