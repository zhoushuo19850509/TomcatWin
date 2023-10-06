package com.nbcb.athena.pipelinedemo;

public interface Pipeline {

    void addValve(Valve valve);
    void setBasic(Valve valve);
}
