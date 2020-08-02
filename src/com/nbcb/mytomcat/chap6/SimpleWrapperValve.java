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
 * ���valve����������������ģ����ǵ���Wrapper�����servlet��service()������
 * Ҳ����ִ�����servlet
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
     * invoke()����������SimpleWrapperValve���basic valveҪ�������飺
     * ����Ҫ����Servlet
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
         * ���Ȼ�ȡ��ǰValve�ҿ����ĸ�container��
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
         * ��BootStrap1�У�SimpleWrapper�����
         * BootStrap1�����õ�servlet nameֱ���õ�Servlet��ʵ���ġ�
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
