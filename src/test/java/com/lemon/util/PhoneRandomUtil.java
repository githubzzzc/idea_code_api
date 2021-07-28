package com.lemon.util;

import java.util.Random;

/**
 * @author shkstart
 * @create 2021-06-17 23:20
 */
public class PhoneRandomUtil {
    public static void main(String[] args) {
//        String unregisterPhone = getUnregisterPhone();
//        System.out.println(unregisterPhone);
//        String randomPhone = getRandomPhone();
//        System.out.println(randomPhone);
    }

    /**
     * 随机手机号
     * @return
     */
    public static String getRandomPhone(){
        Random random = new Random();
        String phonePrefix = "133";
        for(int i = 0; i < 8;i++){
            int num = random.nextInt(9);
            phonePrefix += num;
        }
        return phonePrefix;
    }

    /**
     * 查询数据库是否注册
     * @return
     */
    public static String getUnregisterPhone(){
        String phone = "";
        while(true) {
            //随机数
            phone = getRandomPhone();
            Object result = JDBCUtils.querySingleData("select count(*) from member where mobile_phone = "+phone);
            if((Long)result == 0){
                break;
            }else
                continue;
        }
        return phone;
    }

}
