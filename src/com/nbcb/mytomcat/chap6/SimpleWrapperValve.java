package com.nbcb.mytomcat.chap6;

import org.apache.catalina.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * the basic valve
 * 这个valve做的事情是最基本的：就是调用Wrapper管理的servlet的service()方法，
 * 也就是执行这个servlet
 */
public class SimpleWrapperValve implements Valve, Contained {

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
     * invoke()方法定义了SimpleWrapperValve这个basic valve要做的事情：
     * 就是要调用Servlet
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     * @param context The valve context used to invoke the next valve
     *  in the current processing pipeline
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {

        /**
         * 首先获取当前Valve挂靠在哪个container下
         */
        SimpleWrapper wrapper = (SimpleWrapper) getContainer();

        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();

        HttpServletRequest hsreq = null;
        HttpServletResponse hsres = null;

        if(sreq instanceof HttpServletRequest){
            hsreq = (HttpServletRequest) sreq;
        }

        if(sres instanceof HttpServletResponse){
            hsres = (HttpServletResponse) sres;
        }

        /**
         * 在BootStrap1中，SimpleWrapper会根据
         * BootStrap1中设置的servlet name直接拿到Servlet类实例的。
         *
         */
        Servlet servlet = wrapper.allocate();
        if(hsreq != null && hsres != null){
            servlet.service(hsreq,hsres);
        }else{
            servlet.service(sreq,sres);
        }
        System.out.println("finish invoke the servlet instance");

    }
}
