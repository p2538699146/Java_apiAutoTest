package com.autotest.util;

import java.io.*;
import java.util.*;

/**
 * 读取文本
 */
public class ReadFileUtil {

    public static Map<String, String> fileFieldsValue = new HashMap<>();//保存txt文本读取的数据
    static final String file = "D:\\Java_apiauto_test\\src\\test\\java\\com\\autotest\\config\\FilePath";//保存路径的txt


    //使用静态代码块，每次调用对象自动加载数据
    static {
        read(file);
    }


    /**
     *传入文本路径获取文件地址，或传入文件名+路径获取文件地址
     * @param filePath  文件路径
     * @param fileName  文件名
     * @return
     */
    public static String read(String filePath, String...fileName) {
        BufferedReader br = null;
        try {
            //指定要读取的文件路径
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

           /* char[] cbuf = new char[500];//初始化char数组长度
            int len;//保存char数组长度
            String str = null;//buffer
            while((len = inputStreamReader.read(cbuf)) != -1) {
                str = new String(cbuf, 0, len);
            }*/

            String data = null;
            while ((data = br.readLine()) != null) {
                String[] names = data.split("=");
                for (int i = 0; i < names.length; i++) {
                    if (names[0] != null && names[1] != null) {
                        fileFieldsValue.put(names[0],names[1]);
                    }
                }
            }
            if (fileName != null) {
                return fileFieldsValue.get(fileName);
            } else if (fileName == null && data != null) {//获取到的内容不为空则返回
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "请输入正确的文件路径/要获取的文件名~！";
    }



    /**
     * 传入文件名获取文件路径
     * @param fileName 文件名
     * @return
     */
    public static String getFilePath(String fileName) {
        for (Map.Entry<String, String> entry : fileFieldsValue.entrySet()) {
            if (entry.getKey().contains(fileName)) {
                return entry.getValue();
            }
        }
        //没用找到文件名
        return "";
    }
}
