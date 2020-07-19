package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.chap6.SimpleContext;
import org.apache.catalina.Container;
import org.apache.catalina.Mapper;
import org.apache.catalina.Request;
import org.apache.catalina.Wrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * SimpleContextMapper实现Mapper接口
 * 这个类是用于管理Context下多个Wrapper(servlet)
 */
public class SimpleContextMapper implements Mapper {

    private StandardContext context = null;
    private String protocol = "";

    @Override
    public Container getContainer() {
        return this.context;
    }

    @Override
    public void setContainer(Container container) {
        if(!(container instanceof StandardContext)){
            throw new IllegalArgumentException("illegal type of container");
        }
        this.context = (StandardContext) container;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;

    }

    /**
     * 这个方法的作用是：根据客户端的请求找到对应处理的Servlet类。
     * 具体步骤为：
     * 1.解析客户端请求地址
     * 根据客户端请求URL，解析出servlet相关的URL地址信息，
     * 比如客户端请求为：
     * http://127.0.0.1:8080/servlet/Modern?name=zhoushuo&company=nbcb
     * 那么解析出的servlet相关的URL地址信息为：/Modern
     *
     * 2.然后根据ServletMapping映射信息，址找出对应的Wrapper类
     * 比如之前在BootStrap2中设置了ServletMapping信息为("/Modern","Modern")，
     * 其中"Modern"为Wrapper类的名称
     *
     * 3.最后根据Wrapper类对应的ServletClass字段，找出这个Wrapper负责处理的真正的Servlet类实例
     * 这个映射关系是指BootStrap2中设置的
     *
     * 具体逻辑可以看这个方法，具体调用入口可以参考BootStrap2
     * @param request Request being processed
     * @param update Update the Request to reflect the mapping selection?
     * @return 返回一个Container实例，往往是一个Wrapper实例
     */
    @Override
    public Container map(Request request, boolean update) {

        /**
         * 从客户端请求Request对象中解析出servlet path : /ModernServlet
         */
        // 书本里的代码
//        String contextPath = ((HttpServletRequest)request.getRequest()).getContextPath();
//        String requestURI = ((HttpRequest)request.getRequest()).getDecodedRequestURI();
//        String relativeURI = requestURI.substring(contextPath.length());

        /**
         * 这里为了方便，就沿用Chap4，根据client's http url中截取servletName的方法：
         * request.getRequestURI()为： /servlet/ModernServlet  截取结果为：/ModernServlet
         */
        String servletName = ((HttpServletRequest)request).getRequestURI();
        servletName = servletName.substring(servletName.lastIndexOf("/"));

        Wrapper wrapper = null;
        String servletPath = servletName; // /ModernServlet

        String name = this.context.findServletMapping(servletPath);
        if( name != null){
            wrapper = (Wrapper) context.findChild(name);
        }
        return wrapper;
    }
}
