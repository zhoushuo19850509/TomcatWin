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
 * ���processor����߼���ʵ�ǳ���
 * ���ǽ����ͻ�������url�е�servlet������
 * ���磺 http://127.0.0.1:8080/servlet/PrimitiveServlet
 * Ȼ���ҵ�����˱��ض�Ӧ��PrimitiveServlet.java��
 * ��ͨ������ķ�ʽ����ȡ������ʵ��������ִ��
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
             * respository: ����һ��url��ַ��
             * ��ַ��ʽ��Ҫ�ǰ���fileЭ�����WEB_ROOT�±���õ�servlet class�����磺
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
