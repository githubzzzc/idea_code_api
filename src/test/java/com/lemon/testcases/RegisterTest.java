package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.base.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shkstart
 * @create 2021-06-13 18:28
 */
public class RegisterTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成一个没有被注册的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量
        Environment.envData.put("phone", phone);

    }

    /**
     * 断言
     * @param excelPojo
     */
    @Test(dataProvider = "getRegisterDatas")
    public void testRegister(ExcelPojo excelPojo) {
        //替换用例
         excelPojo = casesReplace(excelPojo);
        //响应数据
        Response res = request(excelPojo,"register");
        //断言
        assertResponse(excelPojo,res );
        //数据库断言
        //从文件读取字段内容
        assertSQL(excelPojo);
    }

    /**
     * 从excel读取指定数据作为参数
     * @return
     */
    @DataProvider
    public  Object[] getRegisterDatas(){
        //读取第一个sheet页；
        List<ExcelPojo> listDatas = readSpecifyExcelData( 1,0);
        //返回一维数组
        return listDatas.toArray();
    }


    @AfterTest
    public void tearDown(){
//        Environment.envData.clear();
    }
}
