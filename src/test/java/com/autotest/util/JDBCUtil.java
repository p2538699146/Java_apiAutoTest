package com.autotest.util;

import org.apache.log4j.Logger;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author shkstart
 * @create 2020-01-12-16:51
 */
public class JDBCUtil {

    public static Logger log = Logger.getLogger(JDBCUtil.class);

    public static final String JDBC_FILECONFIG = ReadFileUtil.getFilePath("jdbc.properties");


    public static Properties properties = new Properties();
    //通过静态代码块解析properties，减少多次读取的磁盘操作
    static {
        InputStream inputStream;
        try {
            //"开始创建输入流，读取jdbc.properties文件
            inputStream = new FileInputStream(JDBC_FILECONFIG);
            //开始加载jdbc.properties文件
            properties.load(inputStream);
        } catch (Exception e) {
            log.error("文件解析异常，请检查文件路径！");
            log.error("报错：" + e);
        }
    }


    /**
     * 根据sql查询表数据，并以map返回，key为字段名，value为字段值
     * @param sql   要执行的查询语句
     * @param value 条件字段的值
     * @return
     */
    public static Map<String, Object> query(String sql , Object ... value) {
        //调用query()，此方法返回map
        InputStream inputStream = null;
        Map<String, Object> columnLabelAndValues = null;
        Connection connection = null;
        try {
            //1.根据连接信息，获得数据库连接（连上数据库）
            connection = getConnection();
            //如果sql不为空则开始提供操作
            if (sql != null || sql.trim().length() > 0) {
                //2.获取PreparedStatement对象（此类型对象有提供数据库的操作方法）
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                log.info("获取数据库操作方法,输入sql语句：" + sql);
                //3.设置条件字段的值：
                for (int i = 0; i < value.length; i++) {
                    preparedStatement.setObject(i + 1, value[i]);
                }
                //4.调用查询方法，执行查询，返回ResultSet（结果集）
                ResultSet resultSet = preparedStatement.executeQuery();
                //获取查询相关的信息
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                //5.从结果集取查询数据
                columnLabelAndValues = new HashMap<>();
                while (resultSet.next()) {
                    //循环取出每个查询字段的数据
                    for (int i = 1; i <= columnCount; i++) {
                        String columnLabel = metaData.getColumnLabel(i);
                        //如果字段值为空，则赋上null值。
                        String columnValue;
                        try {
                            columnValue = resultSet.getObject(columnLabel).toString();
                        } catch (Exception e) {
                            columnValue = null;
                            log.error("查询结果为空,请检查sql：" + sql );
                            log.error("报错内容：" + e);
                        }
                        columnLabelAndValues.put(columnLabel, columnValue);
                        //log.info("将查询字段：" + columnLabel + "的sql的查询结果：" + columnValue + "添加到map");
                    }
                }
            }
        } catch (Exception e) {
            log.error("sql异常，请检查传入的sql：" +  sql);
            log.error("报错内容为：" + e);
        } finally {
            //关闭资源
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return columnLabelAndValues;
    }


    /**
     * 解析数据库连接
     * @return
     */
    public static Connection getConnection()  {
        log.info("开始解析jdbc.properties文件");
        //从properties取出url
        String url = properties.getProperty("jdbc.url");
        //从properties取出username
        String name = properties.getProperty("jdbc.username");
        //从properties取出password
        String password = properties.getProperty("jdbc.password");

        Connection connection = null;
        try {
            log.info("开始连接数据库 --> 数据库连接地址["+url+"]，用户名["+name+"]，密码["+password+"]");
            connection = DriverManager.getConnection(url,name,password);
        } catch (Exception e) {
            log.error("请检查连接地址："+url+"、用户名："+name+"、密码："+password+"！");
            log.error("报错内容为：" + e);
        }
        return connection;
    }
}
