package com.nbcb.mytomcat.test;

public class SystemPropTest {
    public static void main(String[] args) {
        System.out.println("hello");
        String userDir = System.getProperty("user.dir");
        System.out.println(userDir);
    }
}
