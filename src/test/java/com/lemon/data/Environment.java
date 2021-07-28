package com.lemon.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2021-06-14 12:20
 */
public class Environment {
//    public static int memberId;
//    public static String token;
    public static Map<String,Object> envData =  new HashMap<String,Object>();

    public static void main(String[] args) {
        System.out.println(envData);
    }
}

