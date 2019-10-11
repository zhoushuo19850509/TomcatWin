package com.nbcb.mytomcat.chap4.client;


import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 通过http client组件 来访问我们本地的tomcat服务
 */
public class HttpClientTest {

    public void startAccessTomcat(){
        /**
         * 创建默认的httpclient对象
         */
        CloseableHttpClient httpclient =
                HttpClients.createDefault();


        /**
         * 尝试创建不带自动重传功能的httpclient
         */
//        CloseableHttpClient httpclient = HttpClientBuilder.create().disableAutomaticRetries().build();

        startAccessTomcat(httpclient);
    }

    public void startAccessTomcat(CloseableHttpClient httpclient){

        /**
         * 设置一下httpclient的重传参数
         */
//        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0,false);
//        ((AbstractHttpClient)httpclient).setHttpRequestRetryHandler(retryHandler);


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
        HttpGet httpGet = new HttpGet(url);

        /**
         * 设置httpclient请求的参数
         */
//        RequestConfig requestConfig = RequestConfig.custom()
//                // 设置连接超时时间(单位毫秒)
//                .setConnectTimeout(2000)
//                // 设置请求超时时间(单位毫秒)
//                .setConnectionRequestTimeout(2000)
//                // socket读写超时时间(单位毫秒)
//                .setSocketTimeout(2000)
//                // 设置是否允许重定向(默认为true)
//                .setRedirectsEnabled(true).build();
//
//        // 将上面的配置信息 运用到这个Get请求里
//        httpGet.setConfig(requestConfig);

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
        String content = null;

        /**
         * 调用httpclient访问tomcat服务是否成功
         */
        boolean isSucc = false;

        /**
         * 如果调用httpclient访问tomcat服务失败，那么重传3次
         * count代表重传次数
         */
        int count = 0;

        do{
            try {
                response = httpclient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                content = EntityUtils.toString(httpEntity);

                /**
                 * 程序能够运行到这里而不报错，说明访问tomcat成功
                 */
                isSucc = true;
//                System.out.println("第" + (count + 1) + "次访问tomcat成功！" );
            } catch (IOException e) {
//                System.out.println("catch error msg: " + e.getMessage());
//                e.printStackTrace();
                System.out.println("第" + (count + 1) + "次 fail to access tomcat！" );
            }

        }while(!isSucc && (++count < 3) );

        /**
         * 最后，校验一下返回结果
         * 如果content为空，或者content没有包含我们想要的结果，都要报错
         */
        if(null == content || !content.contains("zhoushuo")) {
            System.out.println("Httpclient response return error finally===============>");
        }
        try {
            if(response != null){
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void main(String[] args){
        HttpClientTest client = new HttpClientTest();
        client.startAccessTomcat();
    }
}
