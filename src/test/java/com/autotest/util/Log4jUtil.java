package com.autotest.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class Log4jUtil {

    public static Logger log = Logger.getLogger(Log4jUtil.class);



    /**
     * 以日志的形式输出http请求信息
     * @param response 响应体
     */
    public static void clientLog(CloseableHttpResponse response, String result) {
        //获取http请求状态码
        int statusCode = response.getStatusLine().getStatusCode();
        log.info("\n=========================================================>请求结果<======================================================"
                + "\n=========================================================>状态码["+statusCode+"]<==================================================="
                + "\n=========================================================>头信息<========================================================"
                +"\n" + Arrays.toString(response.getAllHeaders())+""
                + "\n=========================================================>响应体<========================================================"
                +"\n"+ result +
                "\n========================================================================================================================");
    }

}
