package com.nbcb.mytomcat.test;



public class Test1 {
    public static void main(String[] args){
        String servletName = "/servlet/ModernServlet";
        servletName = servletName.substring(servletName.lastIndexOf("/"));
        System.out.println(servletName);
    }
}
