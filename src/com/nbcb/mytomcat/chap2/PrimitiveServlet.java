package com.nbcb.mytomcat.chap2;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * primary servlet that serve the client request
 * this servlet implement the interface "Servlet"
 * the most import function that must implement is service()
 *
 */
public class PrimitiveServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

        /**
         * do something here
         */
        System.out.println("servicing by the primary servlet");
        PrintWriter out = servletResponse.getWriter();
        out.println("hello hello ,I'm the servlet");
        out.println("very glad to see you");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("destroyed");

    }
}
