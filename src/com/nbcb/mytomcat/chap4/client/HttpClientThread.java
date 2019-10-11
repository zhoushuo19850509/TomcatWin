package com.nbcb.mytomcat.chap4.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpClientThread implements Runnable {

    /**
     * ��ǰ�߳�ѭ�����ж��ٴ�http client ���ö���
     */
    private int loopCount;

    /**
     * ��ʶ��ǰ�̵߳ı��
     */
    private int threadId;

    /**
     * ����httpclient���ʷ���˵Ĳ�����
     */
    private HttpClientTest httpClientTest;

    /**
     * httpclient����
     * ��Ҫע����ǣ�Ϊ����������Ч�ʣ�
     * һ��(HttpClientThread)�߳�ֻ��ʼ��һ��CloseableHttpClient����
     * ����ÿ���߳���ÿ��ѭ����Ҫ����CloseableHttpClient����Ч��̫����
     */
    private CloseableHttpClient httpclient;


    /**
     * constructor
     */
    public HttpClientThread(int loopCount, int threadId){
        this.loopCount = loopCount;
        this.threadId = threadId;
        this.httpClientTest = new HttpClientTest();

        this.httpclient =
                HttpClients.createDefault();

    }


    @Override
    public void run() {
        long start = System.currentTimeMillis();
        for(int i = 0 ; i < loopCount; i++){
            httpClientTest.startAccessTomcat(this.httpclient);
//            httpClientTest.startAccessTomcat();
        }
        long end = System.currentTimeMillis();
        System.out.println(" thread " + this.threadId + " finish the task ,avg time cost: " + (end - start)/loopCount + " ms");

        /**
         * �̵߳���󣬲Źر�httpclient����
         */
        if(httpclient != null){
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
