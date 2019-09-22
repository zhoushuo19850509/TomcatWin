package com.nbcb.mytomcat.chap3;

public class BootStrap {

    public static void main(String[] args){
        HttpConnector connector = new HttpConnector();
        connector.start();
    }

}
