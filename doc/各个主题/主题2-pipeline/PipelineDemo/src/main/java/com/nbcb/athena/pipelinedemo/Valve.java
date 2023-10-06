package com.nbcb.athena.pipelinedemo;

public interface Valve {

    Valve getNext();
    void setNext(Valve valve);

}
