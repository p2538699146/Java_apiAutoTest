package com.autotest.cases;


import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.WriteBackData;
import com.autotest.util.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.autotest.util.ExcelUtil.*;

/**数据提供类
 * @author shkstart
 * @create 2020-01-06-2:22
 */
public class RegisterTest extends BaseTest {


    /**
     * 注册接口测试
     * @param Case_ID   用例id
     * @param API_ID    接口id
     * @param paramter  接口参数
     */
    @Test(dataProvider = "Register")
    public void test_Register(String Case_ID, String API_ID,String paramter , String expectedResponseData,
                              String preValidateSql, String afterValidateSql) {

        //执行接口调用前，数据查询
        if (preValidateSql != null && preValidateSql.trim().length() > 0) {
            //接口调用前查询想要验证的字段
            String preValidateResult = DBCheckUtil.doQuery(preValidateSql);
            System.out.println(preValidateResult);
            ExcelUtil.writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID, EXCEL_DATA_PREVALIDATERESULT, preValidateResult));
        }
        //通过apiId获取接口请求地址
        String url = RestUtil.getUrlByApiId(API_ID);
        //通过apiId获取接口请求类型
        String type = RestUtil.getTypeByApiId(API_ID);
        paramter = Global_VariableUtil.replaceVariables(paramter);
        //通过firstJson将参数解析到map中
        Map<String, Object> params = (Map<String, Object>) JSONObject.parse(paramter);
        //调用doService()完成接口调用，拿到接口响应报文
        String result = HttpUtil.doService(type,url,params);
        //调用自定义断言类
        result = AssertUtil.assertEquals_Error_code(result, expectedResponseData);
        //保存每次请求的result,为回写数据对象
        ExcelUtil.writeBackDataArrayList.add(new WriteBackData(ExcelUtil.EXCEL_SHEET_NAME_CASE, Case_ID, ExcelUtil.EXCEL_WRITE_RESULT_CELLNAME, result));
        //执行接口调用后，数据验证
        if (afterValidateSql != null && afterValidateSql.trim().length() > 0) {
            //接口调用后查询想要验证的数据
            String afterValidateResult = DBCheckUtil.doQuery(afterValidateSql);
            System.out.println(afterValidateResult);
            ExcelUtil.writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID, EXCEL_DATA_AFTERVALIDATERESULT, afterValidateResult));
        }
    }


    /**
     * 测试数据提供
     * @return
     */
    @DataProvider(name = "Register")
    public Object[][] datas_Register() {
        Object[][] body = CaseUtil.getCaseDataByAPI_ID("3",cellName);
        return body;
    }
}
