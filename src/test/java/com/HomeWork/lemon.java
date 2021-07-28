package com.HomeWork;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-07-08 18:18
 */
public class lemon {
    /**
     * 注册
     * type
     */
    @Test
    public void resonseRegister(){
        String json = "{\"mobile_phone\":\"13323231132\",\"pwd\":\"12345678\",\"type\":0,\"reg_name\":\"\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().
                        log().all().extract().response();
    }

    @Test
    /**
     * 登录
     */
    public void resonseLogin(){
        String json = "{\"mobile_phone\":\"13323231137\",\"pwd\":\"12345678\",\"type\":0,\"reg_name\":\"\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().
                        log().all().extract().response();
        //
        String json1 = "{\"mobile_phone\":\"13323231137\",\"pwd\":\"12345678\"}";
        Response res1 =
                given().
                        body(json1).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().
                        log().all().extract().response();
        int memberId =  res1.jsonPath().get("data.id");
        String token = res1.jsonPath().get("data.token_info.token");
        System.out.println(token);
        //充值
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":1000}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Authorization","Bearer " + token).
                        when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                        then().
                        log().all().extract().response();
        System.out.println("余额为"+res2.jsonPath().get("data.leave_amount"));
    }

}
