package com.autotest.util;

import com.autotest.pojo.WriteBackData;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-01-01-16:34
 */
public class ExcelUtil {

    private static Logger log = Logger.getLogger(ExcelUtil.class);

    public static final String EXCEL_PATH = ReadFileUtil.getFilePath("Case_Service.xlsx"); //excel路径
    public static final String EXCEL_SHEET_NAME_CASE = "Test_Params";//测试用例表单
    public static final String EXCEL_SHEET_NAME_API_ADDRESS = "API_ADDRESS";//api地址表单
    public static final String EXCEL_SHEET_GLOBAL_VARIABLE = "Global_Variable";//用例变量
    public static final String EXCEL_WRITE_RESULT_CELLNAME = "ActualResponseData";//写回测试数据的行标题
    public static final String STRJOINT_SET = "set"; //用于字符串拼接set
    public static final String STRJOINT_GET = "get"; //用于字符串拼接get
    public static final String EXCEL_DATA_CASE_ID = "Case_ID"; // excel中的用例id标题行
    public static final String EXCEL_DATA_DESC = "Desc"; //excel中的用例描述标题行
    public static final String EXCEL_DATA_API_ID = "API_ID"; //excel中的接口id标题行
    public static final String EXCEL_DATA_PARAMS = "Params"; //excel中的请求参数标题行
    public static final String EXCEL_DATA_EXPECTEDRESPONSEDATA = "ExpectedResponseData"; //excel中的期望响应数据标题行
    public static final String EXCEL_DATA_ACTUALRESPONSEDATA = "ActualResponseData"; //写回测试数据的行标题,excel中的实际响应数据标题行
    public static final String EXCEL_DATA_PREVALIDATESQL = "PreValidateSql";//(接口执行前数据库验证结果)
    public static final String EXCEL_DATA_PREVALIDATERESULT = "PreValidateResult";//(接口执行前脚本验证结果)
    public static final String EXCEL_DATA_AFTERVALIDATESQL = "AfterValidateSql";//(接口执行后数据库验证结果)
    public static final String EXCEL_DATA_AFTERVALIDATERESULT = "AfterValidateResult";//(接口执行后脚本验证结果)
    public static final String EXCEL_DATA_REFLECTVALUE = "ReflectValue";//反射得到的变量值
    public static Map<String, Integer> rowIdRowNumMapping = new HashMap<>();//保存行索引
    public static Map<String, Integer> caseNameCellNumMapping = new HashMap<>();//保存caseName-->列名
    public static List<WriteBackData> writeBackDataArrayList = new ArrayList<>(); //用户保存响应报文，写回excel

    static {
        //开始加载测试数据
        loadRowNumAndCellNumMapping(EXCEL_PATH,EXCEL_SHEET_NAME_CASE);
    }


    /**
     * 获取caseId和对应的行索引
     * 获取cellName和对应的列索引
     * @param excelPath 文件路径
     * @param sheetName 表单名称
     */
    public static void loadRowNumAndCellNumMapping(String excelPath, String sheetName) {
        //调用loadRowNumAndCellNumMapping()，此方法用于获取行标识和索引
        InputStream inputStream = null;
        try {
            //创建一个输入流
            inputStream = new FileInputStream(excelPath);
            //创建workbook对象
            Workbook workbook = WorkbookFactory.create(inputStream);
            //获取要操作的表单对象
            Sheet sheet = workbook.getSheet(sheetName);
            //获取第一行(标题行)
            Row titleRow = sheet.getRow(0);
            //判断titleRow对象数据行不为空
            if (titleRow != null && !isEmptyRow(titleRow)) {
                //获取所有列
                short lastCellNum = titleRow.getLastCellNum();
                //循环处理标题行的每一列
                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    //设置该列的数据为String类型
                    cell.setCellType(CellType.STRING);
                    //获取列的值
                    String title = cell.getStringCellValue();
                    //只取"("之前的数据
                    title = title.substring(0, title.indexOf("("));
                    //获取列索引
                    int cellNun = cell.getAddress().getColumn();
                    caseNameCellNumMapping.put(title, cellNun);
                    //将获取到的："+cellNun+"列，标题行："+title+"，添加到map
                }
            }
            //从第二行开始获取所有的数据行，循环拿到每个数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                //获取每一列的索引值
                Row datatRow = sheet.getRow(i);
                //获取行的第一列
                Cell firstCellOfRow = datatRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //设置该列的值为String类型
                firstCellOfRow.setCellType(CellType.STRING);
                //获取该行的值（用例编号/列标识）
                String rowId = firstCellOfRow.getStringCellValue();
                //如果第一列的值为空则结束循环
                if (rowId == null || rowId.trim().length() == 0) {
                    break;
                }
                //获取行索引
                int rowNum = datatRow.getRowNum();
                rowIdRowNumMapping.put(rowId,rowNum);
                //"将当前循环的，行标识："+rowId+"，行索引：" + rowNum + "，添加到map中保存"
            }
        }catch (Exception e) {
            e.printStackTrace();
            //log.error("运行时异常，报错内容：" + e);
        }finally {
            try {
                //关闭资源
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * excel文件数据读取
     * @param excel_Path  -->文件路径
     * @param rows        --> 行号数组
     * @param cells       --> 列号数组
     * @param getCellName --> excel表单名称
     * @return
     */
    public static Object[][] datas_Excel(String excel_Path, String getCellName, int[] rows, int[] cells) {
        InputStream inputStream = null;
        Object[][] detas = null;
        try {
          //获取一个InputStream输入流,读取路径为：" + excel_Path + "的excel文件！
            inputStream = new FileInputStream(excel_Path);
            //获取workbook对象
            Workbook workbook = WorkbookFactory.create(inputStream);
            //指定表单名称
            Sheet login = workbook.getSheet(getCellName);
            //开始创建Workbook对象,当前操作表单：" + getCellName
            //定义保存对象的数据
            detas = new Object[rows.length][cells.length];
            //通过循获取每一行
            for (int i = 1; i < rows.length; i++) {
                //根据索引取出一行
                Row row = login.getRow(rows[i] - 1);
                //通过循环获取每一列
                for (int j = 1; j < cells.length; j++) {
                    //抛出指针异常
                    try {
                        //根据索引取出每一列，并跳过空参列
                        Cell cell = row.getCell(cells[j] - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (cell == null) {
                            continue;
                        }
                        //将列中参数转为String
                        cell.setCellType(CellType.STRING);
                        String values = cell.getStringCellValue();
                        //根据行列所取数据，保存到datas数组
                        detas[i][j] = values;
                    } catch (NullPointerException e) {
                        log.error("出现空指针，请检查参数！报错：" + e);
                    }
                }
            }
        }catch (Exception e) {
            log.error("运行时异常，报错信息：" + e);
        }  finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return detas;
    }


    /**
     * 加载rests数据
     * @param excelPath --> excel用例路径
     * @param sheetName --> 表单名称
     * @param clazz
     */
    public static  <T> List<T> load(String excelPath, String sheetName, Class<T> clazz) {
        //调用load()，此方法用于加载当前对象：测试对象所需的测试数据
        List<T> list = new ArrayList<>();
        //创建一个输入流
        InputStream inputStream = null;
        try {
           //开始读取当前路径：" + excelPath +"的excel
            inputStream = new FileInputStream(excelPath);
            //创建Workbook对象
            Workbook workbook = WorkbookFactory.create(inputStream);
            //指定表单名
            //开始操作：" + sheetName + "表单
            Sheet sheet = workbook.getSheet(sheetName);
            //获取第一列的索引
            Row titleRow = sheet.getRow(0);
            //获取最后一列的列号
            int lastCellNum = titleRow.getLastCellNum();
            //保存tittle的数组
            String[] fields = new String[lastCellNum];
            //通过循环获取每一列的标题名，并保存进数组
            for (int i = 0; i < lastCellNum; i++) {
                //根据列索引获取对应的列
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //设置列的类型为字符串
                cell.setCellType(CellType.STRING);
                //获取列的值（标题）
                String title = cell.getStringCellValue();
                //只取"("之前的数据
                title = title.substring(0, title.indexOf("("));
                fields[i] = title;
            }
            //定义开始外层循环开始的索引值
            int lastRowIndex = sheet.getLastRowNum();
            //循环获取每一列的数据行
            for (int i = 1; i <= lastRowIndex; i++) {
                T obj = clazz.newInstance();
                //拿到一个数据行
                Row dataRow = sheet.getRow(i);
                //dataRow不为空则拿出excel中得数据行通过反射调用，添加到list
                if (dataRow != null && !isEmptyRow(dataRow)) {
                    //拿到数据行的每一列数据,通过反射将数据到cs对象
                    for (int j = 0; j < lastCellNum; j++) {
                        //根据索引获取对应的列
                        Cell cell = dataRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        //指定列的数据行类型为String
                        cell.setCellType(CellType.STRING);
                        String value = cell.getStringCellValue();
                        //获取要反射的方法名
                        String methodName = STRJOINT_SET + fields[j];
                        //获取要反射的方法对象
                        Method method = clazz.getMethod(methodName, String.class);
                        //完成反射调用
                        method.invoke(obj, value);
                    }
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            log.error("运行时异常，报错内容：" + e);
        } finally {
            //资源关闭
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 判断数据行是否为空  true：为空； fasle：不为空
     * @param dataRow 输入一个Row对象
     * @return
     */
    public static boolean isEmptyRow(Row dataRow) {
        //调用isEmptyRow()，此方法用于判断Row对象是否为空
        int lastCellNum = dataRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = dataRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            String value = cell.getStringCellValue();
            if (value != null && value.trim().length() > 0) {
                return false;
            }
        }
        //log.info("当前对象为空！");
        return true;
    }


    /**
     * 回写接口响应报文
     * @param excelPath excel文件路径
     * @param sheetName 表单名称
     * @param rowId     行索引标识
     * @param cellName  列标题
     * @param result    要写回的数据
     */
    public static void writeBackDatas(String excelPath, String sheetName, String rowId, String cellName, String result) {
        //创建一个输出流
        OutputStream outputStream = null;
        //创建一个输入流
        InputStream inputStream = null;
        try {
            log.info("开始读写路径：" + excelPath + "的excel");
            inputStream = new FileInputStream(excelPath);
            //创建workbook对象
            Workbook workbook = WorkbookFactory.create(inputStream);
            //获取要操作的表单
            Sheet sheet = workbook.getSheet(sheetName);
            log.info("开始创建Workbook对象，当前操作表单：" + sheetName);
            //行索引
            //通过行标识获取行索引
            int rowNum = rowIdRowNumMapping.get(rowId);
            //获取要操作的行
            Row row = sheet.getRow(rowNum);
            //列索引
            //根据列标题获取列索引
            int cellNum = caseNameCellNumMapping.get(cellName);
            //获取操作的列
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            //设置参数类型为String
            cell.setCellType(CellType.STRING);
            //将result设置为cell列的值
            cell.setCellValue(result);
            //创建输出流，用于写入数据
            outputStream = new FileOutputStream(excelPath);
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("运行时异常，报错内容：" + e);
        }finally {
            //资源关闭
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 批量回写测试数据
     * @param excelPath excel路径
     */
    public static void batchWriteBackDatas(String excelPath) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
           //开始读取路径：" + excelPath + "excel"
            //创建一个输入流
            inputStream = new FileInputStream(excelPath);
            Workbook workbook = WorkbookFactory.create(inputStream);
            //遍历writeBackDataArrayList
            for (WriteBackData writeBackData : writeBackDataArrayList) {
                //获取sheetname,根据sheetName找到对应表单
                String sheetName = writeBackData.getSheetName();
                Sheet sheet = workbook.getSheet(sheetName);
               //开始创建Workbook对象，当前操作表单：" + sheetName);
                //获取用例id,根据用例id拿到行索引,获取行
                String rowId = writeBackData.getRowIdentifier();
                int rowNum = rowIdRowNumMapping.get(rowId);
                //根据当前行标识：" + rowId + "获取行索引：" + rowNum
                Row row = sheet.getRow(rowNum);
                //获取列名，根据列名获取列索引
                String cellName = writeBackData.getCellName();
                int cellNum = caseNameCellNumMapping.get(cellName);
                //"根据当前列标识：" + cellName + "，获取当前列索引：" + cellNum
                //判断索引不为空,和列名不为空
                if (cellNum < 0 && cellName == null) {
                    continue;
                }
                //根据索引找到对应列
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //指定cell类型为String
                cell.setCellType(CellType.STRING);
                //设置result为该列的值
                if (writeBackData.getResult() != null) {
                    cell.setCellValue(writeBackData.getResult());
                }
            }
            //创建一个输出流,并回写数据
            outputStream = new FileOutputStream(excelPath);
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("运行时异常，报错内容：" + e);
        } finally {
            //关闭资源
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

