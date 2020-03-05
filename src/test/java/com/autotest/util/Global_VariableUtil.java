package com.autotest.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.Global_Variable;
import com.autotest.pojo.WriteBackData;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.*;

import static com.autotest.util.ExcelUtil.*;

/**
 * 参数化工具类
 * @author shkstart
 * @create 2020-01-12-18:49
 */
public class Global_VariableUtil {

    private static Logger log = Logger.getLogger(Global_Variable.class);

    public static Map<String, String> varableNameAndValuesMap = new HashMap<>();//储存变量名和变量值
    public static List<Global_Variable> variables = new ArrayList<>();//保存excel读取中的数据

    static {
        //加载表单里的数据将每一行封装成对象，最后统一添加到集合
        //开始加载测试数据
        List<Global_Variable> list = ExcelUtil.load(EXCEL_PATH, EXCEL_SHEET_GLOBAL_VARIABLE, Global_Variable.class);
        variables.addAll(list);
        //将变量及变量的值加载到map集合
        loadVariablesToMap();
        ExcelUtil.loadRowNumAndCellNumMapping(EXCEL_PATH,EXCEL_SHEET_GLOBAL_VARIABLE);
    }

    /**
     * 遍历变量集合,将对应的变量名和变量值保存到map中
     */
    public static void loadVariablesToMap() {
        //调用loadVariablesToMap()，此方法遍历variables集合变量名和变量值保存到map
        for (Global_Variable variable : variables) {
            //获取到变量名
            String variableName = variable.getName();
            //log.info("当前变量名：" + variableName);
            //获取到变量名的value值
            String variableValue = variable.getValue();
            if (variableValue == null || variableValue.trim().length() == 0) {
                //要反射的类
                String reflectClass = variable.getReflectClass();
                //要反射的方法
                String reflectMethod = variable.getReflectMethod();
                try {
                    //通过反射获取字节码clazz
                    Class clazz = Class.forName(reflectClass);
                    //通过反射创建对象
                    Object obj = clazz.newInstance();
                    //获取反射调用方法对象的method
                    Method method = clazz.getMethod(reflectMethod);
                    //反射调用方法，获取方法的返回值
                    variableValue = (String) method.invoke(obj);
                    //如果值为空，结束当前循环
                    if (variableValue == null) {
                        continue;
                    }
                    //保存写回数据到集合，批量回写测试数据
                    writeBackDataArrayList.add(new WriteBackData(EXCEL_SHEET_GLOBAL_VARIABLE,variableName
                            ,EXCEL_DATA_REFLECTVALUE, variableValue));
                } catch (Exception e) {
                    log.error("出现异常，报错：" + e);
                    /*e.printStackTrace();*/
                }
            }
            varableNameAndValuesMap.put(variableName, variableValue);
        }
    }


    /**
     * 替换参数里的变量：一次只能替换一个变量
     * @param paramter
     */
    public static String replaceVariables(String paramter) {
        //调用replaceVariables()，此方法用于替换参数中的变量

        //取出所有的变量名
        Set<String> varbleNames = varableNameAndValuesMap.keySet();
        for (String varbleName : varbleNames) {
            //判断如果测试数据中出现了变量名
            if (paramter.contains(varbleName)) {
                log.info("当前变量名：" + varbleName + "替换后的变量值为：" + varableNameAndValuesMap.get(varbleName));
                try {
                    paramter = paramter.replace(varbleName, varableNameAndValuesMap.get(varbleName));
                } catch (NullPointerException e) {
                    log.error("当前操作出现空指针，请检查报错：" + e);
                }
            } else {
                continue;
            }
        }
        return paramter;
    }


    /**
     * 替换多参数，将json字符串转换为map，最后添加进paramter
     * @param paramter jsonString参数
     * @return
     */
    public static String replaceJSONAll(String paramter) {
        //取出所有的变量名
        Set<String> varbleNames = varableNameAndValuesMap.keySet();
        Map<String,Object> params =  JSONObject.parseObject(paramter);
        for (String varbleName : varbleNames) {
            //判断如果测试数据中出现了变量名
            if (paramter.contains(varbleName)) {
                if (paramter.contains(varbleName)) {
                    log.info("当前变量名：" + varbleName + "替换后的变量值为：" + varableNameAndValuesMap.get(varbleName));
                    Map<String,Object> jsonmap = JSONObject.parseObject(varableNameAndValuesMap.get(varbleName));
                    for (Map.Entry<String,Object> jsonParamter : jsonmap.entrySet()) {
                        params.put(jsonParamter.getKey(),jsonParamter.getValue());
                    }
                }
                paramter = JSON.toJSONString(params);
            } else {
                continue;
            }
        }
        return paramter;
    }

      /*public static void main(String[] args) {
        String params = "{\"app_key\":\"941EADA3DF57BE2DFB0920398B6466D6\",\"role\":\"${role}\",\"password\":\"a294c3130b3b2ae755985442aa\"}\n";
      *//*    Set<String> keySet = varableNameAndValuesMap.keySet();
          Map<String,Object> jsonmap = JSONObject.parseObject(a);
          for (String varbleName : keySet) {
              if (params.contains(varbleName)) {
                  for (Map.Entry<String,Object> jsonParamter : jsonmap.entrySet()) {
                      parse.put(jsonParamter.getKey(),jsonParamter.getValue());
                  }
              }
              params = JSON.toJSONString(parse);
          }*//*
          String s = replaceJSONAll(params);
          System.out.println(s);
    }*/
}

