package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.base.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.*;


import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-06-13 18:28
 */
public class LoginTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成一个没有被注册的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量
        Environment.envData.put("phone", phone);
        //读取文件中的指定数据row；
        List<ExcelPojo> listDatas =  readSpecifyExcelData(2,0,1);
        //获取第一行数据
        ExcelPojo excelPojo = listDatas.get(0);
        //请求之前参数替换替换
         excelPojo = casesReplace(excelPojo);
        //读取文件中的第几行数据；作为请求参数
//        Response res = request(listDatas.get(0));
        Response res = request(excelPojo,"login");
        //提取注册后的手机号码，保存到环境变量中
//        extractToEnvironment(listDatas.get(0), res);
        extractToEnvironment(excelPojo, res);
    }

    /**
     * 断言
     * @param excelPojo
     */
    @Test(dataProvider = "getLoginDatas")
    public void testAssert(ExcelPojo excelPojo) {
        //替换用例
         excelPojo = casesReplace(excelPojo);
        //响应数据
        Response res = request(excelPojo,"login");
        //断言
        assertResponse(excelPojo,res );
        assertSQL(excelPojo);
    }

    /**
     * 从excel读取指定数据作为参数
     * @return
     */
    @DataProvider
    public  Object[] getLoginDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData( 2, 1, 11);
        //返回一维数组
        return listDatas.toArray();
    }

    @AfterTest
    public void tearDown(){
    }


    //test
    @Test(dataProvider = "getLoginDatas")
    public void extractToEnvironment(Response res,ExcelPojo excelPojo) {
        String inputParams = excelPojo.getInputParams();
        System.out.println(inputParams);
        Map<String,Object> map = JSONObject.parseObject(inputParams);
        Set<String> allKeySets = map.keySet();
        System.out.println(allKeySets);
        //遍历allkeySets集合，得到value
        for(String key : allKeySets){
            //key为变量名，value为提取的gpath表达式
            Object value = map.get(key);
            Object actualValue = res.jsonPath().get((String) value);
            Environment.envData.put(key,actualValue);
        }
    }

}

