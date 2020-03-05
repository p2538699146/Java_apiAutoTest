package com.autotest.cases;

import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.WriteBackData;
import com.autotest.util.AssertUtil;
import com.autotest.util.CaseUtil;
import com.autotest.util.Global_VariableUtil;
import com.autotest.util.HttpUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.autotest.util.ExcelUtil.*;
import static com.autotest.util.RestUtil.getTypeByApiId;
import static com.autotest.util.RestUtil.getUrlByApiId;

public class getUserInfoData extends BaseTest {

    /**
     * 获取用户个人信息接口
     * @param CaseID    用例id
     * @param API_ID    接口id
     * @param Params    测试数据
     * @param ExpectedResponseData  预期结果
     */
    @Test(dataProvider = "getUserInfo",description = "获取用户个人信息")
    public void getUserInfoData_Test(String CaseID, String API_ID, String Params,
                                     String ExpectedResponseData) {
        String url = getUrlByApiId(API_ID);
        String type = getTypeByApiId(API_ID);
        Params = Global_VariableUtil.replaceJSONAll(Params);
        //利用jsonObject反序列化为一个对象
        Map<String, Object> paramter = JSONObject.parseObject(Params);
        String result = HttpUtil.doService(type, url, paramter);
        result = AssertUtil.assertEquals_Error_code(result,ExpectedResponseData);
        writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_NAME_CASE, CaseID,
                EXCEL_WRITE_RESULT_CELLNAME, result));
    }

    /**
     * DataProvider：数据提供者
     * @return
     */
    @DataProvider(name = "getUserInfo")
    public Object[][] getUserInfo_Data() {
        //调用getCaseDataByAPI_ID根据接口id和excel标题行获取测试数据
        Object[][] data = CaseUtil.getCaseDataByAPI_ID("6", cellName_1);
        return data;
    }
}
