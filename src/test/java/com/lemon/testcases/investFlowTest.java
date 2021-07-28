package com.lemon.testcases;

import com.lemon.base.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-17 18:12
 */
public class investFlowTest extends BaseTest{
    @BeforeClass
    public void setUp(){
        //未注册的手机号
        String borrowerPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        String investPhone = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量，第一个参数为文件的值；
        Environment.envData.put("borrower_phone", borrowerPhone);
        Environment.envData.put("admin_phone", adminPhone);
        Environment.envData.put("invest_phone", investPhone);

        //读取指定数据
        List<ExcelPojo> list = readSpecifyExcelData(5, 0, 9);
        //读取数据，发送请求
        for(int i = 0; i < list.size(); i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"investFlow");
            //提取响应数据
            if(excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo, res);
            }
        }
    }
    @Test
    public void Test(){
        List<ExcelPojo> list = readSpecifyExcelData(5,0,9);
        ExcelPojo excelPojo = list.get(0);
        //替换
        excelPojo = casesReplace(excelPojo);
        //发送请求
        Response res = request(excelPojo,"investFlow");
        //断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);
    }
    @BeforeTest
    public void tearDown(){

    }
}
