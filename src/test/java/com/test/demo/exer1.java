package com.test.demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;



/**
 * @author shkstart
 * @create 2021-07-17 16:41
 */
public class exer1 {
    @Test
    public void resonseLogin(){
        String json6 = "{\"mobile_phone\":\"13323231329\",\"pwd\":\"lemon123456\"}";
        Response res6 = RestAssured.
                given().
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        body(json6).
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        Object uid = res6.jsonPath().get("data.id");
        Object tokenValue = res6.jsonPath().get("data.token_info.token");
        System.out.println(uid);
        //充值
        String jsonData = "{\"member_id\":+\"uid\",\"amount\":10000.0}";
        RestAssured.
                given().
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Authorization","Bearer " + tokenValue).
                        body(jsonData).
                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                        log().all().extract().response();
    }
}
