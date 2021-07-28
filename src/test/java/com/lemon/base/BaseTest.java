package com.lemon.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import com.alibaba.fastjson.JSONObject;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * jsonpath路径表达式，获取到json节点值
 */

/**
 * @author shkstart
 * @create 2021-06-14 17:15
 */
public class BaseTest {
    @BeforeTest
    public void GlobalSetup() throws Exception {
        //返回数据bigdecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //url变量,自动拼接url
        RestAssured.baseURI = Constants.BASE_URL;

    }
    /**
     * 对get，post，patch的二次封装，获取参数返回结果
     * @param excelPojo
     * @return
     */
    public Response request(ExcelPojo excelPojo,String interfaceModuleName){
        String logFilePath;
        if(Constants.LOG_TO_FILE){
        File dirPath = new File(System.getProperty("user.dir") + "\\log\\"+interfaceModuleName);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
        logFilePath = dirPath + "\\test"+excelPojo.getCaseId()+".log";
        PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config =RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));

        }

        //请求网址
        String url = excelPojo.getUrl();
        //请求方法
        String method = excelPojo.getMethod();
        //请求头
        String header = excelPojo.getRequestHeader();
        //请求参数
        String params = excelPojo.getInputParams();
        //请求参数转成map类型
        Map<String,Object> headerMap = JSONObject.parseObject(header, Map.class);
        Response res = null;
        //比较文件中的请求方法
        if("get".equalsIgnoreCase(method)){
            //响应结果
            res = given().headers(headerMap).log().all().when().get(url).then().log().all().extract().response();
        }else if("post".equalsIgnoreCase(method)){
            res = given().headers(headerMap).body(params).log().all().when().post(url).then().log().all().extract().response();
        }else if("patch".equalsIgnoreCase(method)){
            res = given().headers(headerMap).body(params).log().all().when().patch(url).then().log().all().extract().response();
        }
        return res;
    }

    /**
     * 读取文件所有数据，字符串类型需要手动加引号；环境变量"{{}}"也需要加双引号
     * @param sheetNum
     * @return
     */
    public List<ExcelPojo> readAllExcelData( int sheetNum){
        File file = new File(Constants.EXCEL_FILE_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum-1);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas;
    }

    /**
     * 读取指定的数据
     * @param sheetNum sheet编号
     * @param startRow
     * @param readNum
     * @return
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow, int readNum){
        File file = new File(Constants.EXCEL_FILE_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum-1);
        importParams.setStartRows(startRow);
        importParams.setReadRows(readNum);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas;
    }

    /**
     * 从指定行读取到最后一行
     * @param sheetNum
     * @param startRow
     * @return
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow){
        File file = new File(Constants.EXCEL_FILE_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum-1);
        importParams.setStartRows(startRow);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas;
    }

    /**
     * 对应的接口返回字段存到环境变量中；从文件中提取返回结果的提取表达式；一般是cookie，有上下文关联的；
     * @param excelPojo 提取返回json字符串
     * @param res 接口返回response对象
     */
    public void extractToEnvironment(ExcelPojo excelPojo, Response res){
        if(excelPojo.getExtract() != null) {
            //读取文件的实际值，保存到map
            Map<String, Object> extractMap = JSONObject.parseObject(excelPojo.getExtract(), Map.class);
            //循环遍历extractMap
            //获取key
            for (String key : extractMap.keySet()) {
                //提取文件的值（返回结果中的值data.id）作为jsonpath的参数，表达式；
                Object path = extractMap.get(key);
                //根据【提取返回数据】里面的路径表达式去提取实际接口对应返回字段的值(data.id)
                Object value = res.jsonPath().get(path.toString());
                //保存到环境变量中
                Environment.envData.put(key, value);
            }
        }
    }

    /**
     * 从环境变量的值进行正则替换
     * @param orgStr 原始字符串
     * @return 替换后的字符串
     */

    public static String regexReplace(String orgStr ) {
        if (orgStr != null) {
            //正则表达式匹配器
            Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
            //匹配原始字符串
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            while (matcher.find()) {
                //获取到包括{}的内容
                String outerStr = matcher.group(0);
                //获取到{}里的内容，作为环境变量的参数
                String innerStr = matcher.group(1);
                //从环境变量根据参数取到实际的值作为参数
                Object replaceStr = Environment.envData.get(innerStr);
                //实际的值替换文件{}的内容
                result = result.replace(outerStr, replaceStr + "");
            }
            return result;
        }
        return orgStr;
    }

    /**
     * 对用例数据进行替换;从文件中读取参数，调用正则进行替换
     * @param excelPojo
     * @return
     */
    public ExcelPojo casesReplace(ExcelPojo excelPojo){
        //获取输入参数，正则表达式替换参数。读取文件参数进行正则替换，
        String inputParams = regexReplace(excelPojo.getInputParams());
        //写入替换文后件的参数到excelpojo
        excelPojo.setInputParams(inputParams);

        //获取请求参数，正则表达式替换参数。
        String requestHeader = regexReplace(excelPojo.getRequestHeader());
        //替换文件的参数
        excelPojo.setRequestHeader(requestHeader);

        //获取url参数，正则表达式替换url参数。
        String url = regexReplace(excelPojo.getUrl());
        //替换文件的参数
        excelPojo.setUrl(url);

        //获取期望参数，正则表达式替换参数。
        String expected = regexReplace(excelPojo.getExpected());
        //替换文件的参数
        excelPojo.setExpected(expected);

        //数据库断言,
        String dbAssert = regexReplace(excelPojo.getDbAssert());
        excelPojo.setDbAssert(dbAssert);

        return excelPojo;
    }

    /**
     * 对响应结果断言
     * @param excelPojo 用例数据实体类对象
     * @param res 接口响应
     */
    public void assertResponse(ExcelPojo excelPojo,Response res){
        if(excelPojo.getExpected() != null) {
            //读取的数据转成map类型
            Map<String, Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected(), Map.class);
//            遍历map中的值
            for (String key : expectedMap.keySet()) {
                //从文件获取期望结果
                Object expectValue = expectedMap.get(key);
                //接口的实际返回结果(表达式)
                Object actualValue = res.jsonPath().get(key);
                Assert.assertEquals(actualValue, expectValue);
            }
        }
    }

    /**
     *数据库断言
     * @param excelPojo
     */
    public void assertSQL(ExcelPojo excelPojo){
        String dbAssert = excelPojo.getDbAssert();
        if(dbAssert != null) {
            //从文件读取的数据转成map类型
            Map<String, Object> map = JSONObject.parseObject(dbAssert, Map.class);
            //读取map的key集合
            Set<String> keys = map.keySet();
            for (String key : keys) {
                //key sql语句
                //value 数据库的期望值
                Object expectedValue = map.get(key);
//                System.out.println("期望值" + expectedValue.getClass());
                if(expectedValue instanceof BigDecimal){
                    Object actualValue = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actualValue, expectedValue);
                }
                else if(expectedValue instanceof Integer) {
                    //从excle读取的是integer类型
                    //从数据库读到的是lang类型
                    long expectedValue2 = ((Integer)expectedValue).longValue();
                    Object actualValue = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actualValue, expectedValue2);
                }
            }
        }
    }

    /**
     *加密
     */
//    public void signRSA(){
//        //获取时间戳
//        long timestamp = System.currentTimeMillis() / 1000;
//        //获取环境变量的token
//        String token = (String)Environment.envData.get("token");
//        String preStr = token.substring(0, 49);
//        //拼接token和时间戳
//        String str = preStr + timestamp;
//        //通过RSA加密算法对拼接的结果进行加密，得到sign签名
//        String sign = RSAmanager.encryptWithBase64(str);
//        //保存到环境变量
//        Environment.envData.put("timestamp",timestamp);
//        Environment.envData.put("sign",sign );
//    }

}
//    mvn io.qameta.allure:allure-maven:serve

//全局重定向输出到指定文件
//        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new RequestLoggingFilter(fileOutPutStream));
//用例日志单独保存(需要在rest-assured请求和响应中添加log)
//        PrintStream fileOutPutStream1 = new PrintStream(new File("log/test_all.log"));
//        RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream1));
