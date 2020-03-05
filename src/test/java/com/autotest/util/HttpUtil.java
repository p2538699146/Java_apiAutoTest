package com.autotest.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import javax.xml.ws.RequestWrapper;
import java.io.IOException;
import java.util.*;

import static com.autotest.util.Log4jUtil.clientLog;
import static com.autotest.variable.getResponseParamters.*;


/**
 * 用于对接口发起请求
 * @author shkstart
 * @create 2019-12-31-17:16
 */
public class HttpUtil {

    private static Logger log = Logger.getLogger(HttpUtil.class);
    public static Map<String, String> cookies = new HashMap<>(); //储存cookie集合
    public static Map<String, String> token = new HashMap<>();  //储存token的集合
    public static Map<String,Object>  result = new HashMap<>();//储存每次请求的响应结果



    /**
     * post请求：处理from-data格式参数
     * @param url     接口请求地址
     * @param params 接口请求参数
     * @return
     */
    @RequestWrapper()
    public static String doPost_fromdata(String url, Map<String, Object> params) {
        //指定请求方式
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> paramenters = new ArrayList<>();
        //指定请求头
        httpPost.addHeader("User-agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        //根据获取得数据，添加请求头
        addCookieInRequestHeaderBeforeRequest(httpPost);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                 paramenters.add(new BasicNameValuePair(entry.getKey(),entry.getValue() + ""));
            }
        }

        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramenters, "UTF-8"));
            client = HttpClients.createDefault();
            //发起请求，获取接口响应信息(状态码，响应报文，或某些特殊的响应头数据）
            response = client.execute(httpPost);
            //判断请求头中是否带有Set-cookie
            getAndStoreCookiesFromResponseHeader(response);
            //获取响应体
            result = EntityUtils.toString(response.getEntity());
            //输出请求结果
            clientLog(response, result);
            getResultToken(result);  //判断result中的返回结果是否带有token
            getResultByMap(result);  //根据传入的响应体，取出data反序列化为map
        } catch (Exception e) {
            log.error("运行时异常，报错内容：" + e);
        } finally {
            //资源关闭
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //返回响应结果
        return result;
    }



    /**
     * post请求：处理 json 格式参数
     * @param url     接口请求地址
     * @param params  接口请求参数
     * @return
     */
    @RequestWrapper()
    public static String doPost_json(String url, Map<String, Object> params) {
        //指定请求方式
        HttpPost httpPost = new HttpPost(url);
        //指定请求头
        httpPost.addHeader("User-agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
        httpPost.addHeader("Content-Type","application/json");
        httpPost.addHeader("platformCode","weixin");
        //根据获取得数据，添加请求头
        addCookieInRequestHeaderBeforeRequest(httpPost);

        //将参数转为json格式
        JSONObject jsonObject = new JSONObject(params);
        StringEntity stringEntity = new StringEntity(jsonObject.toString(),"UTF-8");

        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String result = null;
        try {
            httpPost.setEntity(stringEntity);
            client = HttpClients.createDefault();
            //发起请求，获取接口响应信息(状态码，响应报文，或某些特殊的响应头数据）
            response = client.execute(httpPost);
            //判断请求头中是否带有Set-cookie
            getAndStoreCookiesFromResponseHeader(response);
            //获取响应体
            result = EntityUtils.toString(response.getEntity());
            //输出请求结果
            clientLog(response, result);
            getResultToken(result); //判断result中的返回结果是否带有token
            getResultByMap(result); //根据传入的响应体，取出data反序列化为map
        } catch (Exception e) {
            log.error("运行时异常，报错内容：" + e);
        } finally {
            //资源关闭
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //返回响应结果
        return result;
    }



    /**
     *get请求
     * @param url 接口请求地址
     * @param params 请求参数
     * @return
     */
    public static String doGet(String url, Map <String, Object> params){

        int number = 1;
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {

               /* if (number == 1) {
                url += "?" + name + "=" + params.get(name);
                } else {
                url += "&" + name + "=" + params.get(name);
                }*/

                //如果number=1，那就代表再url与参数的拼接处，以"？"拼接
                url += (number == 1) ? ("?" + entry.getKey() + "=" + entry.getValue()) : ("&" + entry.getKey() + "=" + entry.getValue());
                number++;
            }
        }
        //指定接口请求方式
        HttpGet httpGet = new HttpGet(url);
        //设置请求头
        //httpGet.addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        httpGet.addHeader("Content-Type","application/json");
        httpGet.addHeader("platformCode","weixin");
        addCookieInRequestHeaderBeforeRequest(httpGet);
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String result = "";
        try {
            //发送请求获取响应数据实体
            client = HttpClients.createDefault();
            //将获取到得数据，添加到请求头
            addCookieInRequestHeaderBeforeRequest(httpGet);
            response = client.execute(httpGet);
            //获取http响应实体
            result = EntityUtils.toString(response.getEntity());
            //输出请求结果
            if (response.getStatusLine().getStatusCode() != 200) {
                log.info("当前请求url和params：" + url);
            }
            clientLog(response, result);
            getResultToken(result); //如果result中有有token，就添加到map
            getResultByMap(result); //根据传入的响应体，取出data反序列化为map
        } catch (Exception e) {
            log.error("运行时异常，请检查报错：" + e);
        } finally {
            //资源关闭
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }



    /**
     * 判断map中是否存有值，如果不为空设置为请求头
     * @param request http请求返回得结果
     */
    public static void addCookieInRequestHeaderBeforeRequest(HttpRequest request) {
        //调用addCookieInRequestHeaderBeforeRequest()，此方法用于添加请求头
        String jsessionid = cookies.get("JSESSIONID");
        String getToken = HttpUtil.token.get("token");
        if (jsessionid != null) {
            log.info("添加JSESSIONID->"+ jsessionid +"<-到请求头");
            request.addHeader("JSESSIONID",jsessionid);
        } else if (getToken != null) {
            //"添加token到请求头"
            request.addHeader("token",getToken);
        }
    }



    /**
     * 拆分获取response的返回结果，将token添加到map
     * @param result  Http请求返回result结果
     */
    public static void getResultToken(String result) {
        //调用getResultValueLoad()，此方法用于获取result中的token

        //将result转为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            //取出key为data的数据
            Object data = jsonObject.get("data");
            //data不为空则转型为map
            if (data != null) {
                JSONObject jsonData = JSONObject.parseObject(data.toString());
                String tokenName = "token";
                Object getToken = jsonData.get(tokenName);
                    if (getToken != null) {
                       //"将当前token：[" + getToken.toString() + "]，添加到map储存"
                        token.put(tokenName, getToken.toString());
                    }
                }
        }catch (NullPointerException e) {
            log.warn("空指针异常，请查看报错：" + e);
        }
    }


    /**
     * 获取响应头数据
     * @param response
     */
    public static void getAndStoreCookiesFromResponseHeader(CloseableHttpResponse response) {
        //调用getAndStoreCookiesFromResponseHeader()，此方法用于获取响应头数据

        //从响应头里取出cookie的请求头
        Header setCookiesHeader = response.getFirstHeader("Set-Cookie");
        //判断请求的cookie不为空
        if (setCookiesHeader != null) {
            //取出响应头的值
            String cookiePairsString = setCookiesHeader.getValue();
            //判断cookie的value不为空
           if (cookiePairsString != null && cookiePairsString.length() > 0) {
               //根据";"切分value
               String[] cookiePairs = cookiePairsString.split(";");
               if (cookiePairs != null) {
                   for (String  cookiePair : cookiePairs) {
                       //如果请求头包含JSESSIONID，则意味着响应头里面有会话id这个数据
                       if (cookiePair.contains("JSESSIONID")) {
                           log.info("取出JSESSIONID，将值："+cookiePair+"保存map");
                           //保存到map
                            cookies.put("JSESSIONID",cookiePair);
                       }
                   }
               }
           }
        }
    }


    /**
     *判断是get或post(json/from-data)请求
     * @param type 接口请求类型
     * @param url  接口请求地址
     * @param params 请求携带参数
     * @return
     */
    public static String doService(String type, String url , Map<String , Object> params) {
        String result = "";
        if ("post_f".equalsIgnoreCase(type)) {
            result = HttpUtil.doPost_fromdata(url, params);
        } else if ("post_j".equalsIgnoreCase(type)) {
            result = HttpUtil.doPost_json(url, params);
        } else if ("get".equalsIgnoreCase(type)) {
            result = HttpUtil.doGet(url, params);
        }
        return result;
    }

}
