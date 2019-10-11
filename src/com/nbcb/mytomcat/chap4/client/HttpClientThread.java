package com.nbcb.mytomcat.chap4.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpClientThread implements Runnable {

    /**
     * 当前线程循环运行多少次http client 调用动作
     */
    private int loopCount;

    /**
     * 标识当前线程的编号
     */
    private int threadId;

    /**
     * 调用httpclient访问服务端的测试类
     */
    private HttpClientTest httpClientTest;

    /**
     * httpclient对象
     * 需要注意的是，为了提升访问效率，
     * 一个(HttpClientThread)线程只初始化一个CloseableHttpClient对象
     * 否则每个线程重每次循环都要创建CloseableHttpClient对象效率太低了
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
         * 线程的最后，才关闭httpclient对象
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
