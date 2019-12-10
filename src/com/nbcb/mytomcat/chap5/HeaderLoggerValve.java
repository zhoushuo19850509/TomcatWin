package com.nbcb.mytomcat.chap5;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * the valve that print the http header(s)
 */
public class HeaderLoggerValve implements Valve, Contained {

    protected Container container;

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {
        context.invokeNext(request,response);
        System.out.println("Valve HeaderLoggerValve.invoke() ...");
        ServletRequest sreq = request.getRequest();
        if(sreq instanceof HttpServletRequest){
            HttpServletRequest hreq = (HttpServletRequest) sreq;
            Enumeration headerNames = hreq.getHeaderNames();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement().toString();
                String headverValue = hreq.getHeader(headerName);
                System.out.println(headerName + " : " + headverValue);
            }
        }else{
            System.out.println("Not http request ,HeaderLoggerValve print nothing!");
        }
        System.out.println("--------------------------------------");
    }
}
