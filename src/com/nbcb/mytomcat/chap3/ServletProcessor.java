package com.nbcb.mytomcat.chap3;


import com.nbcb.mytomcat.util.Constants;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * 第三章的ServletProcessor
 * 功能还是和第二章的差不多：实例化一个本地的Servlet类，来处理客户端请求
 */
public class ServletProcessor {

    /**
     * 根据HttpRequest/HttpResponse 实例化一个本地Servlet类，
     * 处理来自客户端的Servlet请求
     * @param request
     * @param response
     */
    public void process(HttpRequest request, HttpResponse response){

        /**
         * the uri part of the client request:
         * /servlet/hello/PrimitiveServlet
         */

        URLClassLoader loader = null;
        try {
            String uri = request.getRequestURI();

            /**
             * parse the uri and get the servlet name:
             * PrimitiveServlet
             */
            String servletName = uri.substring(uri.lastIndexOf("/") + 1);

            System.out.println("servletName: " + servletName);


            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);

            String repository = (new URL("file",null,
                    classPath.getCanonicalPath() + File.separator)).toString();

            /**
             * respository: 这是一个url地址，
             * 地址形式主要是按照file协议访问WEB_ROOT下编译好的servlet class，比如：
             * file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/
             */
            System.out.println("repository: " + repository);
            urls[0] = new URL(null,repository,streamHandler);
            loader = new URLClassLoader(urls);

            Class myClass = null;

            try {
                myClass = loader.loadClass(servletName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Servlet servlet = null;

            try {
                servlet = (Servlet)myClass.newInstance();
                servlet.service((HttpRequest) request,(HttpResponse) response);
                response.finishResponse();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
