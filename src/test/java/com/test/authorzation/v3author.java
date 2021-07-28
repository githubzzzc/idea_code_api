package com.test.authorzation;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-06-20 15:42
 */
public class v3author {
//    String mobile_phone = "13300001333";
//    String pwd = "12345678";
    int type = 0;
    int memberId;
    String token;
    @Test()
    /**
     * 登录
     */
    public void testLogin() {
        String json = "{\"mobile_phone\":\"13300001333\",\"pwd\":\"12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                when().
                        post("HttpExer://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        memberId = res.jsonPath().get("data.id");
        token = res.jsonPath().get("data.token_info.token");
        System.out.println(token.substring(0,49));
        //
    }
    @Test(dependsOnMethods = "testLogin")
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
                        post("HttpExer://api.lemonban.com/futureloan/member/recharge").
                then().
                        log().all().extract().response();
        System.out.println("余额为" + res2.jsonPath().get("data.leave_amount"));
    }
}
