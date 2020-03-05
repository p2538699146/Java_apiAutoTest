package com.autotest.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * 自定义类库：断言比较实际测试结果和预期测试结果
 * @author shkstart
 * @create 2020-01-09-1:03
 */
public class AssertUtil {

    private static Logger log = Logger.getLogger(AssertUtil.class);


    /**
     *比较预期结果和时间结果并返回result
     * @param actualResponse       实际结果
     * @param expectedResponseData 预期结果
     * @return
     */
    public static String assertEquals(String actualResponse, String expectedResponseData) {
        //调用自定义断言类assertEquals
        String result = "通过";
        try {
            //调用testNG得断言比较实际结果和预期结果
            Assert.assertEquals(actualResponse, expectedResponseData);
        } catch (Throwable t) {
            result = actualResponse;
            log.warn("断言实际结果与预期结果不一致，请检查写回的参数并查询数据库！");
        }
        return result;
    }


    /**
     *传入result获取error_code，通过error_code,比较预期和实际结果
     * @param acErro_code   实际结果
     * @param exError_code  预期结果
     * @return
     */
    public static String assertEquals_Error_code(String acErro_code, String exError_code) {
        //调用assertEquals_Error_code方法，此方法只断言err_code！
        String ac = getError_code(acErro_code);
        String ex = getError_code(exError_code);
        String result = "通过";
        //如果实际响应结果中没取出err_code,调用assertEquals方法断言全部返回数据
        if (ac == null) {
            result = assertEquals(acErro_code,exError_code);
            return result;
        }
        try {
            Assert.assertEquals(ac,ex);
        } catch (Throwable t) {
            result = acErro_code;
            log.warn("断言实际结果与预期结果不一致，实际返回err_code：" + ac);
        }
        return result;
    }


    /**
     * 传入result得到一个String的err_code
     * @param result JSON格式字符串
     * @return
     */
    public static String getError_code(String result) {
        //将result转为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            Object data = jsonObject.get("meta");
            if (data != null) {
                JSONObject jsonData = JSONObject.parseObject(data.toString());
                Object err_code = jsonData.get("code");
                if (err_code != null) {
                        return err_code.toString();
                    }
                }
        } catch (Exception e) {
            log.error("运行时异常，报错：" + e);
        }
        return null;
    }
}
