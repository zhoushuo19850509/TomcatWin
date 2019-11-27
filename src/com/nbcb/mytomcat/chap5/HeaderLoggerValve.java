package com.nbcb.mytomcat.chap5;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * the valve that print the http header
 */
public class HeaderLoggerValve implements Valve, Contained {
    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {

    }
}
