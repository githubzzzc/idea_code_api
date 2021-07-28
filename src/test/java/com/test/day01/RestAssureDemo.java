package com.test.day01;



import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-06-02 17:43
 */
public class RestAssureDemo {
    public static void main(String[] args) {
        given().
                when().
                        get("https://www.baidu.com").
                then().log().all();
    }

    /**
     * 表单
     */

    @Test
    public void postDome1(){
        given().formParam("moblie_phone","123456").
                formParam("pwd","123456").
                when().
                    post("http://www.httpbin.org/post").
                then().
                log().all();
    }

    /**
     * json
     */
    @Test
    public void postDome2(){
        String jsonData = "{\"mobile_phone\":\"123456\"}";
            given().
                body(jsonData).
                contentType("application/json").
            when().
                post("http://www.httpbin.org/post").
            then().
                log().all();
    }

    /**
     * 响应数据
     */
    @Test
    public void responseJson1(){
        Response res =
        given().
        when().
            post("http://www.httpbin.org/post").
        then().
            log().all().extract().response();
        System.out.println(res.time());
    }
    @Test
    public void responseJson2(){
        String json = "{\"mobile_phone\":\"13323231111\",\"pwd\":\"12345678\"}";
        Response res =
            given().
                body(json).
                header("Content-Type","application/json").
                header("X-Lemonban-Media-Type","lemonban.v1").
            when().
                post("http://api.lemonban.com/futureloan/member/login").
            then().
                log().all().extract().response();
        System.out.println(res.jsonPath().get("data.id")+"");
    }
    @Test
    public void responseJson3(){
        Response res =
                given().
                        when().
                        get("http://www.httpbin.org/json").
                        then().
                        log().all().extract().response();
        System.out.println(res.time());
        List<String> list = res.jsonPath().get("slideshow.slides.title");
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
    @Test
    public void responsehtml(){
        Response res =
                given().
                        when().
                        get("http://www.baidu.com").
                        then().
                        log().all().extract().response();
        System.out.println(res.time());
        System.out.println(res.htmlPath().get("html.head.meta[0].@content")+"");
    }
    @Test
    public void responsexml(){
        Response res =
                given().
                        when().
                        get("http://www.httpbin.org/xml").
                        then().
                        log().all().extract().response();
        System.out.println(res.time());
        System.out.println(res.xmlPath().get("slideshow.slides[1].title")+"");
        System.out.println(res.xmlPath().get("slideshow.slides[1].@type")+"");
    }
    @Test
    /**
     * 登录
     */
    public void resonseLogin(){
        String json = "{\"mobile_phone\":\"13323231111\",\"pwd\":\"12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        int memberId =  res.jsonPath().get("data.id");
        String token = res.jsonPath().get("data.token_info.token");
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
}


