package com.autotest.pojo;

/**
 * @author shkstart
 * @create 2020-01-11-22:41
 */
public class DBCheck {
    private String no; //编号
    private String sql; //sql语句

    public String getNo() {
        return no;
    }

    public String getSql() {
        return sql;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public DBCheck() {
    }

    public DBCheck(String no, String sql) {
        this.no = no;
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "DBCheck{" +
                "no='" + no + '\'' +
                ", sql='" + sql + '\'' +
                '}';
    }
}
