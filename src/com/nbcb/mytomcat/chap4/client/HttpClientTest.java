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
 * ͨ��http client��� ���������Ǳ��ص�tomcat����
 */
public class HttpClientTest {

    public static void startAccessTomcat(){
        CloseableHttpClient httpclient =
                HttpClients.createDefault();

        /**
         * ���ñ���tomcat��servlet����1
         */
        String url = "http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb";

        /**
         * ���ñ���tomcat��servlet����1
         */
//        String url = "http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb";

        /**
         * ����nginx service
         */
//        String url = "http://127.0.0.1/";
//        String url = "http://www.baidu.com";
        HttpGet httpGet = new HttpGet(url);

        /**
         * ����http headers
         */
//        httpGet.setHeader("","");


        /**
         * ����parameters
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
