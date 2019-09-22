package com.nbcb.mytomcat.chap4.client;

public class HttpClientThread extends Thread {

    private int loopCount;
    /**
     * constructor
     */
    public HttpClientThread(int loopCount){
        this.loopCount = loopCount;
    }


    @Override
    public void run() {
        for(int i = 0 ; i < loopCount; i++){
            HttpClientTest.startAccessTomcat();
        }

    }
}
