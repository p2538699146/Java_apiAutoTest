package com.autotest.cases;

import com.autotest.util.*;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import static com.autotest.util.ExcelUtil.*;
import static com.autotest.util.ExcelUtil.EXCEL_DATA_EXPECTEDRESPONSEDATA;

/**
 * @author shkstart
 * @create 2020-01-06-2:28
 */
public class BaseTest {

    public Logger log = Logger.getLogger(BaseTest.class);

    //excel标题行带sql验证
    public String[] cellName = {EXCEL_DATA_CASE_ID, EXCEL_DATA_API_ID,EXCEL_DATA_PARAMS,
            EXCEL_DATA_EXPECTEDRESPONSEDATA,EXCEL_DATA_PREVALIDATESQL,EXCEL_DATA_AFTERVALIDATESQL};
    //excel标题行没有sql验证
    public String[] cellName_1 = {EXCEL_DATA_CASE_ID, EXCEL_DATA_API_ID,EXCEL_DATA_PARAMS, EXCEL_DATA_EXPECTEDRESPONSEDATA};


    /**
     * 用例执行完后，批量写入excel
     */
    @AfterSuite
    public void batchWriteBackDaras() {
        //批量写入excel
        log.info("\n开始将测试结果批量写回excel");
        ExcelUtil.batchWriteBackDatas(EXCEL_PATH);
    }
}
