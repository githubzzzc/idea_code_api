package com.test.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author shkstart
 * @create 2021-07-09 23:03
 */
public class Tcp {
    public static void main(String[] args) throws Exception {
        String ip = "192.168.187.129";
        int port = 111;
        Socket sck = new Socket(ip, port);
        //传输内容
        String content ="这是一个java客户端";
        System.out.println(content);
        //把传输内容转成字节流
        byte[] bstream = content.getBytes("GBK");
        OutputStream os = sck.getOutputStream();
        os.write(bstream);
        sck.close();


    }
}
