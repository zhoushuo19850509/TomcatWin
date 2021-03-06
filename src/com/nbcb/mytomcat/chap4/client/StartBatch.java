package com.nbcb.mytomcat.chap4.client;

/**
 * 这个程序主要是模拟HttpClient并发访问本地Tomcat程序
 * 可以调整并发线程数，每个线程重复运行次数
 *
 * 目标是：
 * 1.验证本地Tomcat支持并发性能；
 * 2.验证本地Tomcat http persistent connection是否对于性能有提升
 */
public class StartBatch {
    public static void main(String[] args){

        /**
         * 并发线程数
         */
        int threadCount = 60;

        /**
         * 每个线程循环发起多少次请求
         */
        int loopCount = 500;

        for(int i = 0 ; i < threadCount ; i++){
//            Thread thread = new HttpClientThread(loopCount,i);
            new Thread(new HttpClientThread(loopCount,i)).start();

        }

    }
}
