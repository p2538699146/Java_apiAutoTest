package com.autotest.cases;

import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.WriteBackData;
import com.autotest.util.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.autotest.util.ExcelUtil.EXCEL_DATA_ACTUALRESPONSEDATA;
import static com.autotest.util.ExcelUtil.EXCEL_SHEET_NAME_CASE;


/**
 * @author shkstart
 * @create 2020-01-07-21:43
 */
public class WeatherTest extends BaseTest {


    /**
     * 天气接口测试
     * @param Case_ID  用例id
     * @param API_ID   接口id
     * @param Paramter 接口参数
     */
    @Test(dataProvider = "Weather",description = "天气接口测试")
    public void test_Weather(String Case_ID, String API_ID, String Paramter, String expectedResponseData) {

        //根据API_ID获取接口地址
        String url = RestUtil.getUrlByApiId(API_ID);
        //根据API_ID获取接口类型
        String type = RestUtil.getTypeByApiId(API_ID);
        //将参数通过firstJson解析到map
        Map<String, Object> params = (Map<String, Object>) JSONObject.parse(Paramter);
        //调用doService()完成接口调用，拿到接口响应报文
        String result = HttpUtil.doService(type,url,params);
        //调用自定义断言类，如果结果一致，返回通过
        result = AssertUtil.assertEquals(result, expectedResponseData);
        //保存每次请求的result,为回写数据对象
        ExcelUtil.writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, Case_ID, EXCEL_DATA_ACTUALRESPONSEDATA,result));
    }

    @DataProvider(name = "Weather")
    public Object[][] datas_Weather() {
        Object[][] data = CaseUtil.getCaseDataByAPI_ID("2",cellName_1);
        return data;
    }
}
