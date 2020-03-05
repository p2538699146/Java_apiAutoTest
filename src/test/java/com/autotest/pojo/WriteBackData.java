package com.autotest.pojo;

/**
 * 回写测试数据对象
 * @author shkstart
 * @create 2020-01-06-1:49
 */
public class WriteBackData {
    private String sheetName;       //表单名
    private String rowIdentifier;   //行标识
    private String cellName;        //列标识
    private String result;          //要写回的数据

    public String getSheetName() {
        return sheetName;
    }


    public String getCellName() {
        return cellName;
    }

    public String getResult() {
        return result;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }


    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRowIdentifier() {
        return rowIdentifier;
    }

    public void setRowIdentifier(String rowIdentifier) {
        this.rowIdentifier = rowIdentifier;
    }

    public WriteBackData() {
    }

    /**
     * @param sheetName     表单名
     * @param rowIdentifier 行标识
     * @param cellName      列标识
     * @param result        要写回的数据
     */
    public WriteBackData(String sheetName, String rowIdentifier, String cellName, String result) {
        this.sheetName = sheetName;
        this.rowIdentifier = rowIdentifier;
        this.cellName = cellName;
        this.result = result;
    }

    @Override
    public String toString() {
        return "WriteBackData{" +
                "sheetName='" + sheetName + '\'' +
                ", rowIdentifier='" + rowIdentifier + '\'' +
                ", cellName='" + cellName + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
