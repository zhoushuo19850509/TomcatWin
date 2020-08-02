package com.nbcb.mytomcat.test;


import com.nbcb.mytomcat.chap3.ModernServlet;
import com.nbcb.mytomcat.util.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * 这个测试类主要是为了验证class loader的功能
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        System.out.println("start test");


        URL[] urls = null;
        try {
            urls = new URL[1];
            URLStreamHandler streamHandler = null;
            String classRepository = System.getProperty("user.dir") + "/WEB-INF/classes";

            File classPath = new File(classRepository);

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
            urls[0] = new URL("file:/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes/");
            URLClassLoader classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());


            String servletName = "ModernServlet";
            Class<ModernServlet> myClass = null;

            try {
                myClass =    (Class<ModernServlet>) classLoader.loadClass(servletName);
                System.out.println("class resource of the servlet instance: " + myClass.getResource("/"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
