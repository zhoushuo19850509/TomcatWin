package com.nbcb.mytomcat.chap4.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Servlet defined in chap4
 */
public class MyServlet extends HttpServlet {
    /**
     * constructor
     * do nothing
     */
    public MyServlet() {
    }

    public void init(ServletConfig var1) {
        System.out.println("ModernServlet -- init");
    }

    /**
     * 这个doGet方法是这个Servlet的主方法
     * 这个方法里的东西，主要是打印HttpServletRequest对象的内容
     * HttpServletRequest是我们之前根据客户端Http请求解析出来的内容，详细请参考HttpRequest.java
     * 因为之前HttpRequest就是实现了HttpServletRequest接口。
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
//        OutputStream outputStream = response.getOutputStream();
//        outputStream.write();

        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Modern Servlet1</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h2>Headers</h2>");


        /**
         * 获取request中的http header的字段，将header的key/value打印出来
         */
        Enumeration headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            writer.println("<br>" + headerName +  "</br>"); //  " : " + request.getHeader(headerName) + "</br>");
        }

        writer.println("<br><h2>Method</h2>");
        writer.println("<br>" + request.getMethod() + "</br>");
        writer.println("<br><h2>Parameters</h2></br>");


        /**
         * 打印客户端http请求的parameter names
         */
        Enumeration parameterNames = request.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            String parameterName = (String)parameterNames.nextElement();
            writer.println("<br>" + parameterName +  "</br>");  // " : " + request.getParameter(parameterName) + "</br>");
        }

        writer.println("<br><h2>Query String</h2></br>");
        writer.println("<br>" + request.getQueryString() + "</br>");
        writer.println("<br><h2>Request URI</h2></br>");
        writer.println("<br>" + request.getRequestURI() + "</br>");
        writer.println("</body>");
        writer.println("</html>");
    }

}
