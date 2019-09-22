package com.nbcb.mytomcat.chap2;


import com.nbcb.mytomcat.chap1.Request;
import com.nbcb.mytomcat.chap1.Response;
import com.nbcb.mytomcat.util.Constants;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * this processor(version 1.0)
 * is used to process the client's servlet requests
 *
 * 这个processor类的逻辑其实非常简单
 * 就是解析客户端请求url中的servlet类名称
 * 比如： http://127.0.0.1:8080/servlet/PrimitiveServlet
 * 然后，找到服务端本地对应的PrimitiveServlet.java，
 * 并通过反射的方式，获取这个类的实例，并且执行
 */
public class ServletProcessor1 {

    /**
     *
     * @param request
     * @param response
     */
    public void process(Request request, Response response)  {
        /**
         * the uri part of the client request:
         * /servlet/hello/PrimitiveServlet
         */

        URLClassLoader loader = null;
        try {
            String uri = request.getUri();

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
                servlet.service((ServletRequest) request,(ServletResponse) response);
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

    public static void main(String[] args){
        ServletProcessor1 processor1 = new ServletProcessor1();
        Request request = new Request();
        request.setUri("/servlet/hello/PrimitiveServlet");
        Response response = new Response(null);
        processor1.process(request,response);

    }

}
