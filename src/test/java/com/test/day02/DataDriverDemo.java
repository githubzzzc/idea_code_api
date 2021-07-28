package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.lemon.pojo.ExcelPojo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.reporters.jq.Main;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author shkstart
 * @create 2021-06-13 13:37
 */
public class DataDriverDemo {
    @Test(dataProvider = "getLoginDatas02")
    public void testAssert(ExcelPojo excelPojo) {
        //返回数据bigdecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //url变量
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //网址
        String url = excelPojo.getUrl();
        //参数
        String inputParams = excelPojo.getInputParams();
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //map
        Map  requestHeaderMap = (Map) JSON.parse(requestHeader);
        //获取响应结果
        String expected = excelPojo.getExpected();
        //把响应结果转成map
        Map <String,Object>expectedMap = (Map) JSON.parse(expected);

        Response res =
                given().
                        body(inputParams).
                        headers(requestHeaderMap).
                        when().
                        post(url).
                        then().
                        log().all().extract().response();

        for(String key : expectedMap.keySet()){
            //获取期望结果
            Object expectValue = expectedMap.get(key);
            //接口的实际返回结果
            Object actualValue = res.jsonPath().get(key);
            Assert.assertEquals(actualValue,expectValue);
        }
    }
//    @DataProvider
//    public  Object[][] dataLogin(){
//        Object[][] data = {
//                {"13323231111","123456"},
//                {"1332323111","123456"},
//                {"13323231111","12345678"}
//        };
//        return data;
//    }

    //excle
//    @DataProvider
//    public  Object[][] getLoginDatas01(){
//        Object[][] data = {
//                {"13323231111","123456"},
//                {"1332323111","123456"},
//                {"13323231111","12345678"}
//        };
//        return data;
//    }

    /**
     * 读取excel文件，设置读取的sheet页，传入参数
     * @return
     */
    @DataProvider
    public  Object[] getLoginDatas02(){
        File file = new File("G:\\019柠檬班\\04练习\\前程贷web端-系统测试用例--完整版 - 接口.xlsx");
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
//        new ImportParams().setStartSheetIndex();
        //excel pojo类的集合

        List <ExcelPojo> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        return listDatas.toArray();
    }

//    public static void main(String[] args) {
//        File file = new File("G:\\019柠檬班\\04练习\\前程贷web端-系统测试用例--完整版 - 接口.xlsx");
//        ImportParams importParams = new ImportParams();
//        importParams.setStartSheetIndex(1);
//        List <Object> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
//        for(Object object : listDatas){
//            System.out.println(object);
//        }
//    }
}