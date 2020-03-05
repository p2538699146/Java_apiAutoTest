package com.autotest.variable;

import com.alibaba.fastjson.JSONObject;
import com.autotest.util.ReadFileUtil;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class getResponseParamters {

    private static Logger log = Logger.getLogger(getResponseParamters.class);//初始化日志
    public static Map<String, String> stockpileResultData = new HashMap<>();//储存每次api请求的响应体
    public static String getResultKey; //赋值字段名，获取result中的变量
    public static String urlFilePath = ReadFileUtil.getFilePath("testUrl.properties");//储存url的文件路径
    public static List<String> list = new ArrayList();//保存上游接口的key：value
    static Properties properties = new Properties();//创建properties操作文件

    //加载testUrl文件的数据
    static {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(urlFilePath);
            properties.load(inputStream);
        } catch (Exception e) {
            log.error("请查propertites文件路径！");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 获取上游接口参数，解决接口依赖
     * @return
     */
    public static String getResponseParamter() {
        Set<Map.Entry<String, String>> entrySet = stockpileResultData.entrySet();
        Map<String,Object> map = new HashMap<>();
        for (Map.Entry<String,String> entry : entrySet) {
            if (list.size() > 1) {
                for (String fields : list) {
                    map.put(fields, stockpileResultData.get(fields));
                }
            } else if (entry.getKey() != null && entry.getValue() != null) {
                return  stockpileResultData.get(getResultKey);
            }
        }
        Object jsonMap = JSONObject.toJSON(map);
        //没有获取到内容
        return jsonMap.toString();
    }


    /**
     * 输入接口名称获取请求地址,根据uri给getResultKey赋值
     * @param uriName 接口名称
     * @return
     */
    public static String readParametersValue(String uriName) {
        String uri = properties.getProperty(uriName);
        //接口响应字段
        uriName += "-response";
        String property = properties.getProperty(uriName);
        if (property != null) {
            String[] split = property.split(",");
            if (split.length > 1) {
                list.addAll(Arrays.asList(split));
            } else if(split.length == 1) {
                getResultKey = property;
            }
        }
      /* if (property != null) {
           getResultKey = property;
       }*/
        return uri;
    }


    /**
     * 根据传入的响应体，取出data反序列化为map
     * @param result 接口请求响应体
     */
    public static void getResultByMap(String result) {
        if (result != null) {
            //将result转为json格式数据，并取出data
            JSONObject jsonData = (JSONObject) JSONObject.parseObject(result).get("data");
            //data不为空则添加到map
            if (jsonData != null) {
                Map<String, Object> jsonMap = new HashMap(jsonData);
                for (Map.Entry<String,Object> entry : jsonMap.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        stockpileResultData.put(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
        }
    }


/*    public static void main(String[] args) {
        String a ="\"{msg:登录成功, role:2}\"";

        String result = "{\n" +
                "  \"meta\": {\n" +
                "    \"success\": true,\n" +
                "    \"message\": \"执行成功\",\n" +
                "    \"code\": \"00000000\"\n" +
                "  },\n" +
                "  \"data\": {\n" +
                "    \"token\": \"front_user_login_weixin_eyJkYXRlIjoxNTgyNTcyNTQ2ODQ0LCJ1aWQiOiIzNDgwN2I5MzkxYmE0YTdjOWUyNDdkNzNiZDJlZDE5MiIsInR5cCI6IkpXVCIsInRpbWUiOjE1ODI1NzI1NDY4NDQsInV1aWQiOiJjZDU1ZGYwNC1iMTJjLTQwMGQtOTNhZS0wNDMwN2RiYjhiOWUiLCJhbGciOiJIUzI1NiJ9eyJuYW1lIjoieXVubmFuemhvbmd5YW4iLCJ0eXBlIjoiand0In0N70XivAg8tjXOrHzMSZPyRYO8fOXY6RVngmLSj0I9s\",\n" +
                "    \"msg\": \"登录成功\",\n" +
                "    \"role\": 2,\n" +
                "    \"isNeedSetPwd\": false,\n" +
                "    \"setPwdToken\": null,\n" +
                "    \"isNeedGetOpenId\": false,\n" +
                "    \"setOpenIdToken\": null,\n" +
                "    \"isNeedUnbindMobile\": false,\n" +
                "    \"unbindMobileToken\": null,\n" +
                "    \"unbindMobile\": null\n" +
                "  }\n" +
                "}";
        getResultByMap(result);
        //String property = properties.getProperty("login-by-moblie-response");
        String s = readParametersValue("login-by-moblie");


        Map<String, Object> map = new HashMap<>();
        for (Map.Entry entry : stockpileResultData.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {

            }
        }

    }*/


    /**
     * String转map
     * @param str
     * @return
     */
    public static Map<String,String> getStringToMap(String str){
        log.info("开始调用getStringToMap()，此方法用于String转map");
        Map<String,String> map = null;
        if (str != null && str.trim().length() > 0) {
            //根据逗号截取字符串数组
            String[] str1 = str.split(",");
            //创建Map对象
            map = new HashMap<>();
            //循环加入map集合
            for (int i = 0; i < str1.length; i++) {
                //根据":"截取字符串数组
                try {
                    String[] str2 = str1[i].split(":");
                    //str2[0]为KEY,str2[1]为值
                    map.put(str2[0], str2[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    log.error("请检查传入参数："+str+"，出现索引越界！");
                    return null;
                }
            }
        }
        return map;
    }

}



