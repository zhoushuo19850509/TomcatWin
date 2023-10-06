package com.nbcb.athena.pipelinedemo;

public class MyBasicValve implements Valve , Lifecycle{
    private Valve next;

    public Valve getNext() {
        return next;
    }

    public void setNext(Valve valve) {
        this.next = valve;
    }

    public void start() {
        System.out.println("MyBasicValve started ...");
    }
}
