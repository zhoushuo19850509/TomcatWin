package com.nbcb.mytomcat.test.thread;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class LibLoaderTest {

    public static void main(String[] args) {
        System.out.println("start test");


        URL[] urls = null;
        try {
            urls = new URL[1];
            URLStreamHandler streamHandler = null;
            String libRepository = System.getProperty("user.dir") + "/WEB-INF/lib/httpclient-4.5.10.jar";
            File classPath = new File(libRepository);

            String repository = null;
            repository = (new URL("file",null,
                    classPath.getCanonicalPath() + File.separator)).toString();


            System.out.println("repository: " +repository);
            /**
             * respository: 这是一个url地址，
             * 地址形式主要是按照file协议访问WEB_ROOT下编译好的servlet class，比如：
             * file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/
             */

//            System.out.println("repository: " + repository);
            urls[0] = new URL("file:" + libRepository);
            URLClassLoader classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());


            String servletName = "org.apache.http.client.ClientProtocolException";
            Class myClass = null;

            try {
                myClass =    classLoader.loadClass(servletName);
                System.out.println("class resource of the servlet instance: " + myClass.getResource("/"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
