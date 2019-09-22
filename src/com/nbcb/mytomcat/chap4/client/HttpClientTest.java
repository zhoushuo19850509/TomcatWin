package com.nbcb.mytomcat.chap4.client;


import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 通过http client组件 来访问我们本地的tomcat服务
 */
public class HttpClientTest {

    public static void startAccessTomcat(){
        CloseableHttpClient httpclient =
                HttpClients.createDefault();

        /**
         * 调用本地tomcat的servlet服务1
         */
        String url = "http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb";

        /**
         * 调用本地tomcat的servlet服务1
         */
//        String url = "http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb";

        /**
         * 本地nginx service
         */
//        String url = "http://127.0.0.1/";
//        String url = "http://www.baidu.com";
        HttpGet httpGet = new HttpGet(url);

        /**
         * 设置http headers
         */
//        httpGet.setHeader("","");


        /**
         * 设置parameters
         */
//        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("name","zs"));


        CloseableHttpResponse response = null;
        try {

            response = httpclient.execute(httpGet);

            System.out.println("http response status info: ");
            System.out.println(response.getStatusLine());

            HttpEntity httpEntity = response.getEntity();
            System.out.println("http response content entity info: ");
            System.out.println(EntityUtils.toString(httpEntity));

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(response != null){
                    response.close();
                }

                if(httpclient != null){
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        HttpClientTest.startAccessTomcat();
    }
}
