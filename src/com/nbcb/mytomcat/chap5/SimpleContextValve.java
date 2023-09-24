package com.nbcb.mytomcat.chap5;

import org.apache.catalina.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The basic valve of SimpleContext's pipeline
 */
public class SimpleContextValve implements Valve, Contained {

    private Container container;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
         this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    /**
     * 这个方法
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     * @param valveContext
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {

        System.out.println("basic valve(SimpleContextValve) invoked ...");
        /**
         * 首先获取当前Valve挂靠在哪个container下
         */
        SimpleContext context = (SimpleContext) getContainer();

        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();

        if( !(sreq instanceof HttpServletRequest) ||
                !(sres instanceof HttpServletResponse)){
            return;
        }


        /**
         * 根据client's request url获取对应Servlet处理类：Wrapper
         */
        Wrapper wrapper = null;
        wrapper = (Wrapper) context.map(request,true);

        /**
         * 然后通过调用Wrapper.invoke()调用Servlet实例
         */
        response.setContext(context);
        wrapper.invoke(request,response);
    }
}
