package com.autotest.pojo;


import java.util.Map;

/**
 * @author shkstart
 * @create 2020-01-12-16:30
 */
public class DBQueryResult {

    /**
     * 脚本编号
     */
    private String no;

    /**
     * 脚本执行查到的数据，保存到map中（key保存的是字段名，value保存的是对应字段查到的数据）
     */
    private Map<String , Object> columenLabelAndValues;

    public DBQueryResult(String no, Map<String, Object> columenLabelAndValues) {
        this.no = no;
        this.columenLabelAndValues = columenLabelAndValues;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setColumenLabelAndValues(Map<String, Object> columenLabelAndValues) {
        this.columenLabelAndValues = columenLabelAndValues;
    }

    public DBQueryResult() {
    }

    @Override
    public String toString() {
        return "DBQueryResult{" +
                "no='" + no + '\'' +
                ", columenLabelAndValues=" + columenLabelAndValues +
                '}';
    }
}
