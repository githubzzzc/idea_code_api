package com.lemon.testcases;

import com.lemon.base.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.lemon.pojo.ExcelPojo;

import java.util.List;

/**
 * @author shkstart
 * @create 2021-06-18 15:52
 */
public class AddloanTest extends BaseTest{
    @BeforeClass
    public void steUp(){
        String borrowserPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone", borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone );
        //读取用例
        List<ExcelPojo> list = readSpecifyExcelData(4,0,4);
        for(int i = 0; i < list.size(); i++){
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"addloan");
            if(excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }
        }
    }
    @Test(dataProvider = "getAddLoanDatas")
    public void testAddLoan(ExcelPojo excelPojo){
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"addloan");
        //断言
        assertResponse(excelPojo,res );
        assertSQL(excelPojo);
    }
    @DataProvider
    public Object[] getAddLoanDatas(){
        List<ExcelPojo> ListDatas = readSpecifyExcelData(4, 0);
        return ListDatas.toArray();
    }
    @AfterTest
    public void teardown(){

    }

}
