package com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AAA {
    public static void main(String[] args){
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("name","zs");
        headers.put("sex","male");
        headers.put("age","19");
        headers.put("nationality","China");

//        List<String> headerNameList = (List<String>)headers.keySet();
        for(String aaa: headers.keySet()){
            System.out.println(aaa);
        }

    }

}
