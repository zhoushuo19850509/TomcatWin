package com.nbcb.mytomcat.chap4;

import org.apache.catalina.LifecycleException;

import java.io.IOException;


public class BootStrap {

    public static void main(String[] args){
        HttpConnector connector = new HttpConnector();
        SimpleContainer container = new SimpleContainer();

        connector.setContainer(container);

        try {
            connector.initialize();
            connector.start();

            /**
             * wait until we press any key
             */
            System.in.read();
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
