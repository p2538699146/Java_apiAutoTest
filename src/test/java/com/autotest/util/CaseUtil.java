package com.autotest.util;

import com.autotest.pojo.Case;
import org.apache.log4j.Logger;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static com.autotest.util.ExcelUtil.*;

/**
 * @author shkstart
 * @create 2020-01-02-21:50
 */
public class CaseUtil {

    private static Logger log = Logger.getLogger(CaseUtil.class);

    public String[] cellName;//标题行
    public String API_ID;//接口id

    public CaseUtil(String[] cellName, String API_ID) {
        this.cellName = cellName;
        this.API_ID = API_ID;
    }

    //保存所有用例数据的集合（共享数据）
    public static List<Case> Cases = new ArrayList<>();
    //通过静态代码块加载数据到Cases
    static {
       //开始加载测试数据，并添加到Cases集合！
        List<Case> list = load(EXCEL_PATH, EXCEL_SHEET_NAME_CASE, Case.class);
        Cases.addAll(list);
    }

    /**
     * @param API_ID    接口类型 ：1：POST, 2: GET
     * @param cellNames  获取指定数据的列名
     * @return
     */
    public static Object[][] getCaseDataByAPI_ID(String API_ID, String[] cellNames) {
        //开始调用getCaseDataByAPI_ID方法，此方法返回Object[][]（指定接口id的数据行）！
        Class<Case> clazz = Case.class;
        //保存指定接口编号的Case对象
        ArrayList<Case> CaseList = new ArrayList<>();
        //通过循环找出指定接口编号对应的url
        for (Case cs : Cases) {
            //循环处理
            //"判断Cases储存数据中的接口id，是否与传入的一致！"
            if (cs.getAPI_ID().equals(API_ID)) {
                //将cs中的数据："+cs+"，添加到集合
                CaseList.add(cs);
            }
        }
        //"定义一个Object二维数组，长度为CaseList.size()，每个数组可存储对象cellNames.length"
        Object[][] datas = new Object[CaseList.size()][cellNames.length];
        for (int i = 0; i < CaseList.size(); i++) {
            //循环获取每一列的数据
            Case cs = CaseList.get(i);
            for (int j = 0; j < cellNames.length; j++) {
                try {
                    //要反射的方法名
                    String methodName = ExcelUtil.STRJOINT_GET + cellNames[j];
                    //获取要反射的对象
                    Method method = clazz.getMethod(methodName);
                    //将反射对象数据保存value
                    String value = String.valueOf(method.invoke(cs));
                    //"将获取到反射对象的数据，保存到data数组："+ value
                    datas[i][j] = value;
                } catch (Exception e) {
                    log.error("运行时异常，报错为：" + e);
                }
            }
        }
        return datas;
    }
}
