package com.HomeWork;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @author shkstart
 * @create 2021-07-08 18:06
 */
public class HttpExer {
    //get
    @Test
    public void responseJson1() {
        Response res =
                given().
                when().
                        get("http://www.httpbin.org/post").
                then().
                        log().all().extract().response();
        System.out.println(res.time());

    }
    //post
    @Test
    public void responseJson2() {
        Response res =
                given().
                when().
                        post("http://www.httpbin.org/post").
                then().
                        log().all().extract().response();
        System.out.println(res.time());

    }
    //put
    @Test
    public void responseJson3() {
        Response res =
                given().
                when().
                        put("http://www.httpbin.org/post").
                then().
                        log().all().extract().response();
        System.out.println(res.time());

    }
    //delete
    @Test
    public void responseJson4() {
        Response res =
                given().
                when().
                        delete("http://www.httpbin.org/post").
                then().
                        log().all().extract().response();
        System.out.println(res.time());

    }
}

