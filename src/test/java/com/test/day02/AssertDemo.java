package com.test.day02;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author shkstart
 * @create 2021-06-13 11:39
 */
public class AssertDemo {
    int memberId;
    @Test
    /**
     * 登录
     */
    public void testAssert() {
        String json = "{\"mobile_phone\":\"13323231333\",\"pwd\":\"12345678\"}";
        //返回数据bigdecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //url变量
        RestAssured.baseURI = "HttpExer://api.lemonban.com/futureloan";
        Response res =
                given().
//                        config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))).
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        when().
                        post("/member/login").
                        then().
                        log().all().extract().response();
        //整数类型
        int code = res.jsonPath().get("code");
        Assert.assertEquals(code,0);
        //字符串类型
        String msg = res.jsonPath().get("msg");
        Assert.assertEquals(msg,"OK");
        //小数
//        float leaveAmount = res.jsonPath().get("data.leave_amount");
//        Assert.assertEquals(leaveAmount,600001.0);
        //对数据有要求用bigdecimal,restAssured,返回类型是float
        BigDecimal actual = res.jsonPath().get("data.leave_amount");
        BigDecimal expected = BigDecimal.valueOf(601001.0);
        Assert.assertEquals(actual,expected);

        //充值
        int memberId = res.jsonPath().get("data.id");
        String token = res.jsonPath().get("data.token_info.token");
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
        BigDecimal expected1 = BigDecimal.valueOf(601001.0);
        BigDecimal actual1 = res.jsonPath().get("data.leave_amount");
        Assert.assertEquals(actual1,expected1);
    }
}
