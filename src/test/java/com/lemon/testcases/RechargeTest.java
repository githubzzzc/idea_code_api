package com.lemon.testcases;

import com.lemon.base.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-06-14 11:42
 */
public class RechargeTest extends BaseTest {
    int memberId;
    String token;

    @BeforeClass
    public void setup() {
        //生成一个没有被注册的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量
        Environment.envData.put("phone", phone);
        //从excel读取的多个数据 listDatas
        List<ExcelPojo> listDatas = readSpecifyExcelData(3, 0, 4);
        //获取第一行数据保存到excelpojo
        ExcelPojo excelPojo = listDatas.get(0);
        //替换
        excelPojo = casesReplace(excelPojo);
        //传入替换后的参数
        Response resRegister = request(excelPojo, "recharge");
        //listdata=excelPojo;文件的数据,字段
//        String extractStr = listDatas.get(0).getExtract();
        //保存到环境变量，注册返回的结果
        extractToEnvironment(listDatas.get(0), resRegister);
        //参数替换{{phone}}
        casesReplace(listDatas.get(1));

        //登录优化后；读取表里第一行的数据作为登录的参数
        Response resLogin = request(listDatas.get(1), "recharge");

        //提取表里第一行的getExtract字段的值
        //提取的数据转成map
        extractToEnvironment(listDatas.get(1), resLogin);
    }

    //    充值，优化
    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        //读取excelpojo类里的请求参数，通过json转成map
        //读取输入参数，替换环境变量memberId的值
        excelPojo = casesReplace(excelPojo);
        //把修改后的参数保存到excel
//        excelPojo.setInputParams(params);
        Response res = request(excelPojo, "recharge");
        //读取文件的实际值，保存到map
        //断言
        assertResponse(excelPojo, res);
        assertSQL(excelPojo);
    }

    //md5加密
//    @Test(dataProvider = "getRechargeDatas")
//    public void testRecharge(ExcelPojo excelPojo) {
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
//
//        excelPojo = casesReplace(excelPojo);
//        //把修改后的参数保存到excel
//        Response res = request(excelPojo,"recharge");
//        //读取文件的实际值，保存到map
//        //断言
//        assertResponse(excelPojo,res );
//        assertSQL(excelPojo);
//}



    @DataProvider//提供指定数据
    public Object[] getRechargeDatas() {
        //读取指定数据，传入参数，文件、sheet、
        List<ExcelPojo> listdatas = readSpecifyExcelData(3, 1, 4);
        //把读取的数据转成数组
        return listdatas.toArray();
    }
}

        //读取的数据转成map类型
//        Map requestHeaderMap1 = JSON.parseObject(listDatas.get(0).getRequestHeader());
//        Map requestHeaderMap2 = JSON.parseObject(listDatas.get(0).getRequestHeader());

        //注册 优化前
//        given().//读取的数据作为参数发送get请求，提交参数
//                body(listDatas.get(0).getInputParams()).
//                headers(requestHeaderMap1).
//        when().
//                post(listDatas.get(0).getUrl()).
//        then().
//                log().all();

        //登录 优化前
//        Response response =
//        given().
//                body(listDatas.get(1).getInputParams()).
//                headers(requestHeaderMap2).
//        when().
//                post(listDatas.get(1).getUrl()).
//        then().
//                log().all().extract().response();

    //充值，优化前
//    @Test(dataProvider = "getRechargeDatas")
//    public void testRecharge(ExcelPojo excelPojo){
//        //读取excelpojo类里的请求参数，通过json转成map
//        //读取输入参数，替换环境变量memberId的值
//        String parms = regexReplace(excelPojo.getInputParams(), Environment.memberId + "");
//        Map requestHeader  = JSON.parseObject(excelPojo.getRequestHeader());
//        given().
//                body(parms).
//                headers(requestHeader).
//                when().
//                post(excelPojo.getUrl()).
//                then().
//                log().all();
//    }


