package com.test.day01;

import io.restassured.internal.ResponseParserRegistrar;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-06-13 11:03
 */
public class lemon {
    String mobile_phone = "13323231132";
    String pwd = "12345678";
    int type = 0;
    int memberId;
    String token;
    @Test(priority=0)
    /**
     * 注册
     * type
     */
    public void resonseRegister(){
        String json = "{\"mobile_phone\":\""+memberId+"\",\"pwd\":\""+pwd+"\",\"type\":"+type+",\"reg_name\":\"\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/register").
                then().
                        log().all().extract().response();
        System.out.println(res);
        String userId = res.jsonPath().get("data.id");
        System.out.println(userId);
    }

//    @Test(priority = 1)
    @Test(dependsOnMethods = "resonseRegister")
    /**
     * 登录
     */
    public void resonseLogin() {
        String json = "{\"mobile_phone\":\""+mobile_phone+"\",\"pwd\":\""+pwd+"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().
                        log().all().extract().response();
        memberId = res.jsonPath().get("data.id");
        token = res.jsonPath().get("data.token_info.token");
        System.out.println(token);
        System.out.println(res);
    }
    @Test(priority = 3)
    //充值
    public void testrecharg () {
        String jsonData = "{\"member_id\":" + memberId + ",\"amount\":1000}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization", "Bearer " + token).
                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                        log().all().extract().response();
        System.out.println("余额为" + res2.jsonPath().get("data.leave_amount"));

    }
}
