package com.nbcb.mytomcat.chap5;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * the valve the print the client's IP
 */
public class ClientIPLoggerValve implements Valve, Contained {

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
        System.out.println("Valve ClientIPLoggerValve.invoke() ...");
        ServletRequest sreq = request.getRequest();
        System.out.println("Client IP : " + sreq.getRemoteAddr());
        System.out.println("--------------------------------------");
    }
}
