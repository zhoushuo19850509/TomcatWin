package com.nbcb.mytomcat.test.thread;

public class StartThread {
    public static void main(String[] args){
        MyThread thread = new MyThread();
        MyThread thread1 = new MyThread();
        thread.run();
        thread1.run();
        System.out.println(thread.toString());
        System.out.println(thread.hashCode());
        System.out.println(thread1.hashCode());
    }
}
