package com.autotest.variable;

import com.autotest.util.JDBCUtil;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 获取注册账号
 * @author shkstart
 * @create 2020-01-13-19:07
 */

public class UserGenerator {

    private static Logger log = Logger.getLogger(UserGenerator.class);

    /**
     * 生成用于注册的账号
     * @return
     */

    public static String generateToBeRegisterUser() {
        //开始获取用于注册的手机号！
        String sql = "select concat(max(user_mobile) + 3,'') as toBeRegisterUser from uc_user_test";
        Map<String, Object> columnLabelAndValue = JDBCUtil.query(sql);
        return columnLabelAndValue.get("toBeRegisterUser").toString();
    }

    /**
     * 生成系统中还未注册的账号
     * @return
     */
    public static String generateSystemNotExistUser() {
        //开始获取未注册的手机号！
        String sql = "select concat(max(user_mobile) + 2,'') as systemNotExistUser from uc_user_test";
        Map<String, Object> columnLabelAndValue = JDBCUtil.query(sql);
        return columnLabelAndValue.get("systemNotExistUser").toString();
    }

}
