package com.test.demo;

import com.lemon.data.Environment;
import org.testng.annotations.Test;

import java.util.*;

/**
 * @author shkstart
 * @create 2021-06-19 14:23
 */
public class tset {
    @Test
    public void test(){
//        Map map = new HashMap();
//        map.put("AA",123 );
//        map.put(45,123 );
//        map.put("BB",123 );
//        map.put("AA",87 );
//        System.out.println(map);

        Map map1 = new HashMap();
        map1.put("AA",123 );
        map1.put("EE",121 );
//        System.out.println(map1);
//        System.out.println(map1.get("EE"));

        //遍历key集，keyset
//        Set set = map1.keySet();
        System.out.println("set="+map1.keySet());
        System.out.println("value"+map1.get("AA"));
//        Iterator iterator = set.iterator();
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//        //遍历value集
//        Collection values = map.values();
//        Iterator iterator1 = values.iterator();
//        while (iterator1.hasNext()){
//            System.out.println(iterator1.next());
//        }
    }
}
