package com.nbcb.mytomcat.test;


import com.nbcb.mytomcat.chap3.ModernServlet;
import com.nbcb.mytomcat.util.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * �����������Ҫ��Ϊ����֤class loader�Ĺ���
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
             * respository: ����һ��url��ַ��
             * ��ַ��ʽ��Ҫ�ǰ���fileЭ�����WEB_ROOT�±���õ�servlet class�����磺
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
