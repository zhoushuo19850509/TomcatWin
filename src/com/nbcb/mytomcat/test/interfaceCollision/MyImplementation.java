package com.nbcb.mytomcat.test.interfaceCollision;

public class MyImplementation implements Interface111,Interface222 {
//    @Override
//    public int getName() {
//        return null;
//    }

    @Override
    public String getName(String aaa) {
        return null;
    }

    @Override
    public String getName(int bbb) {
        return null;
    }
}
