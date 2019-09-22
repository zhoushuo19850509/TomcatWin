package com.nbcb.mytomcat.chap4.client;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通过plain java networking 来访问我们本地的tomcat服务
 */
public class PlainJavaClient {
    public static void main(String[] args){
        System.out.println("start access server!");
        Socket socket = null;
        InputStream in = null;
        Reader r = null;
        try {
            URL u = new URL("http://127.0.0.1/");
//            URL u = new URL("http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb");
            URLConnection uc = u.openConnection();

            /**
             * read the http header of url
             * Connection:keep-alive
               Data:Sat, 14 Sep 2019 09:13:49 GMT
               Etag:"59746037-264"
               Last-modified:Sun, 23 Jul 2017 08:37:11 GMT
               Server:nginx/1.13.3
             */
            System.out.println("Date: " + new Date(uc.getDate()));
            System.out.println("LastModified: " + new Date(uc.getLastModified()));
//            System.out.println(uc.getContent().toString());

            /**
             * read the content of url
             */
//            in = uc.getInputStream();
//            r = new InputStreamReader(in);
//             int c ;
//             while((c = in.read()) != -1){
//                 System.out.print((char)c);
//             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finish access server!");

    }
}
