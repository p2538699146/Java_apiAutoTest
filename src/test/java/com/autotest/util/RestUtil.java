package com.autotest.util;

import com.autotest.pojo.Rest;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.autotest.util.ExcelUtil.*;
import static com.autotest.variable.getResponseParamters.*;


/**
 * @author shkstart
 * @create 2020-01-04-22:28
 */
public class RestUtil {

    private static Logger log = Logger.getLogger(RestUtil.class);

    //用于保存每次加载的测试数据
    public static List<Rest> rests = new ArrayList<>();
    //加载测试数据
    static {
        //开始加载RestUtil对象所需测试数据！
        List<Rest> list = ExcelUtil.load(EXCEL_PATH,EXCEL_SHEET_NAME_API_ADDRESS, Rest.class);
        rests.addAll(list);
    }


    /**
     * 根据接口编号，获取Url请求地址
     * @param API_ID 接口编号
     * @return
     */
    public static String getUrlByApiId(String API_ID) {
        //调用getUrlByApiId()，此方法用于获取接口请求地址
        for (Rest rest : rests) {
           if (rest.getAPI_ID().equals(API_ID)) {
               String url = rest.getURL();
               url += readParametersValue(rest.getAPI_NAME());
               return url;
           }
        }
        log.error("当前测试数据中未找到接口id：" + API_ID + "的请求类地址;");
        return "";
    }


    /**
     * 根据接口编号，获取接口请求类型
     * @param API_ID 接口编号id
     * @return
     */
    public static String getTypeByApiId(String API_ID) {
        //调用getTypeByApiIdd()，此方法用于获取接口请求类型
        for (Rest rest : rests) {
            if (rest.getAPI_ID().equals(API_ID)) {
                return rest.getAPI_TYPE();
            }
        }
        log.error("当前测试数据中未找到接口id：" + API_ID + "的请求类型;");
        return "";
    }


    /**
     * 根据接口编号，获取接口名称
     * @param API_ID  接口编号
     * @return
     */
    public static String getNameByApiId(String API_ID) {
        for (Rest rest : rests) {
            if (rest.getAPI_ID().equals(API_ID)) {
                return rest.getAPI_NAME();
            }
        }
        log.error("没有找到接口名称！");
        return "";
    }
}
