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
 * @create 2020-01-17-22:45
 */
public class UserGetListTest extends BaseTest{


    /**
     *
     * @param Case_ID   用例id
     * @param API_ID    接口id
     * @param paramter 请求参数
     * @param expectedResponseData 预期结果
     */
    @Test(dataProvider = "GetListTest")
    public void test_GetList(String Case_ID, String API_ID, String paramter, String expectedResponseData) {

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
        writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID, EXCEL_WRITE_RESULT_CELLNAME, result));
    }


    @DataProvider(name = "GetListTest")
    public Object[][] data_GetListTest() {
        Object[][] data = CaseUtil.getCaseDataByAPI_ID("4", cellName_1);
        return data;
    }
}
