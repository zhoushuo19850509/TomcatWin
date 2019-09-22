package com.nbcb.mytomcat.test.thread;

public class MyThread implements Runnable{


    @Override
    public void run() {
        System.out.println("new thread running!");
    }
}
