package com.nbcb.athena.pipelinedemo;

public class MyValve2 implements Valve , Lifecycle{
    private Valve next;

    public Valve getNext() {
        return next;
    }

    public void setNext(Valve valve) {
        this.next = valve;
    }

    public void start() {
        System.out.println("MyValve2 started ...");
    }
}
