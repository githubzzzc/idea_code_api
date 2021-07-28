package com.test.demo;




import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2021-07-10 18:36
 */
public class JavaHttpHandle {
    private String globalCookie = "";
    public static void main(String[] args) {
        JavaHttpHandle jhh = new JavaHttpHandle();
//        jhh.sendGet("http://www.baidu.com");
        jhh.sendPost("http://www.baidu.com", "abc");
    }
    public void sendGet(String urlAddress,String cookie){
        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(urlAddress);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setUseCaches(false);
//            urlConnection.setRequestProperty("Cookie","cookie" );
            urlConnection.connect();
            //获取响应内容
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            String line = bf.readLine();
            while(line != null){
                System.out.println(line);
                line = bf.readLine();
                System.out.println(urlConnection.getHeaderField("Set=Cookie"));
                Map<String, List<String>> map = urlConnection.getHeaderFields();
                List list = (List)map.get("Set=Cookie");
                for(int i = 0; i < list.size();i++){
                    String temp = list.get(i).toString();
                    String[] value = temp.split(";");
                    System.out.println(list.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //post
    public void sendPost(String urlAddress,String parmValue){
        try {
            //新建一个url连接
            HttpURLConnection urlConnection = null;
            //新建一个url
            URL url = new URL(urlAddress);
            //url转成连接类型
            urlConnection = (HttpURLConnection)url.openConnection();
            //设置url连接超时
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Cookie","cookie" );
            urlConnection.connect();
            //获取响应内容
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            //从字节流中读取数据
            String line = bf.readLine();
            while(line != null){
                System.out.println(line);
                line = bf.readLine();
                System.out.println(urlConnection.getHeaderField("Set=Cookie"));
                //获取请求信息转成map
                Map<String, List<String>> map = urlConnection.getHeaderFields();
                List list = (List)map.get("Set=Cookie");
                for(int i = 0; i < list.size();i++){
                    String temp = list.get(i).toString();
                    String[] value = temp.split(";");
                    this.globalCookie += value[0] + ";";
                }
                System.out.println(globalCookie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
