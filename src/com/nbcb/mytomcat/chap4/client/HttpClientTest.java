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
 * ͨ��http client��� ���������Ǳ��ص�tomcat����
 */
public class HttpClientTest {

    public void startAccessTomcat(){
        /**
         * ����Ĭ�ϵ�httpclient����
         */
        CloseableHttpClient httpclient =
                HttpClients.createDefault();


        /**
         * ���Դ��������Զ��ش����ܵ�httpclient
         */
//        CloseableHttpClient httpclient = HttpClientBuilder.create().disableAutomaticRetries().build();

        startAccessTomcat(httpclient);
    }

    public void startAccessTomcat(CloseableHttpClient httpclient){

        /**
         * ����һ��httpclient���ش�����
         */
//        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0,false);
//        ((AbstractHttpClient)httpclient).setHttpRequestRetryHandler(retryHandler);


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
         * ����httpclient����Ĳ���
         */
//        RequestConfig requestConfig = RequestConfig.custom()
//                // �������ӳ�ʱʱ��(��λ����)
//                .setConnectTimeout(2000)
//                // ��������ʱʱ��(��λ����)
//                .setConnectionRequestTimeout(2000)
//                // socket��д��ʱʱ��(��λ����)
//                .setSocketTimeout(2000)
//                // �����Ƿ������ض���(Ĭ��Ϊtrue)
//                .setRedirectsEnabled(true).build();
//
//        // �������������Ϣ ���õ����Get������
//        httpGet.setConfig(requestConfig);

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
        String content = null;
        try {

            response = httpclient.execute(httpGet);

//            System.out.println("http response status info: ");
//            System.out.println(response.getStatusLine());

            HttpEntity httpEntity = response.getEntity();
            content = EntityUtils.toString(httpEntity);
//            System.out.println("http response content entity info: ");
//            System.out.println(content);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{

            /**
             * finally��У��һ�·��ؽ��
             * ���contentΪ�գ�����contentû�а���������Ҫ�Ľ������Ҫ����
             */
            if(null == content || !content.contains("zhoushuo")){
                System.out.println("Httpclient response return error===============>");
            }
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        HttpClientTest client = new HttpClientTest();
        client.startAccessTomcat();
    }
}
