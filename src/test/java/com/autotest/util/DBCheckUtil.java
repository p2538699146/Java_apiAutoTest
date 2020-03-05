package com.autotest.util;

import com.alibaba.fastjson.JSONObject;
import com.autotest.pojo.DBCheck;
import com.autotest.pojo.DBQueryResult;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * @author shkstart
 * @create 2020-01-11-22:17
 */
public class DBCheckUtil {

    private static Logger log = Logger.getLogger(DBCheckUtil.class);

    /**
     * 根据脚本执行查询并返回查询结果
     * @param ValidateSql 需要执行的查询语句
     * @return
     */
    public static String doQuery(String ValidateSql) {
        //将脚本字符串封装成对象
        List<DBCheck> dbCheckers = JSONObject.parseArray(ValidateSql, DBCheck.class);
        List<DBQueryResult> dbQueryResults = new ArrayList<>();
        for (DBCheck dbCheck : dbCheckers) {
            //拿到sql的编号
            String no = dbCheck.getNo();
            log.info("获取到sql脚本编号：" + no);
            try {
                //拿到sql脚本
                if (dbCheck.getSql() == null && dbCheck.getSql().trim().length() == 0) {
                    continue;
                }
            } catch (NullPointerException e) {
                log.error("运行时异常，报错：" + e);
            }
            String sql = dbCheck.getSql();
            log.info("获取到sql语句：" + sql);
            //执行查询，就获取到结果
            Map<String, Object> columnLabelAndValues = JDBCUtil.query(sql);
            DBQueryResult dbQueryResult = new DBQueryResult();
            dbQueryResult.setNo(no);
            dbQueryResult.setColumenLabelAndValues(columnLabelAndValues);
            log.info("编号：" + no + "，查询结果：" + columnLabelAndValues);
            //将执行结果添加到dbQueryResults集合中
            dbQueryResults.add(dbQueryResult);
        }
        return JSONObject.toJSONString(dbQueryResults.toString());
    }
}
