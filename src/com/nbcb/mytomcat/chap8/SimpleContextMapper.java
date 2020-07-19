package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.chap6.SimpleContext;
import org.apache.catalina.Container;
import org.apache.catalina.Mapper;
import org.apache.catalina.Request;
import org.apache.catalina.Wrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * SimpleContextMapperʵ��Mapper�ӿ�
 * ����������ڹ���Context�¶��Wrapper(servlet)
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
     * ��������������ǣ����ݿͻ��˵������ҵ���Ӧ�����Servlet�ࡣ
     * ���岽��Ϊ��
     * 1.�����ͻ��������ַ
     * ���ݿͻ�������URL��������servlet��ص�URL��ַ��Ϣ��
     * ����ͻ�������Ϊ��
     * http://127.0.0.1:8080/servlet/Modern?name=zhoushuo&company=nbcb
     * ��ô��������servlet��ص�URL��ַ��ϢΪ��/Modern
     *
     * 2.Ȼ�����ServletMappingӳ����Ϣ��ַ�ҳ���Ӧ��Wrapper��
     * ����֮ǰ��BootStrap2��������ServletMapping��ϢΪ("/Modern","Modern")��
     * ����"Modern"ΪWrapper�������
     *
     * 3.������Wrapper���Ӧ��ServletClass�ֶΣ��ҳ����Wrapper�������������Servlet��ʵ��
     * ���ӳ���ϵ��ָBootStrap2�����õ�
     *
     * �����߼����Կ�������������������ڿ��Բο�BootStrap2
     * @param request Request being processed
     * @param update Update the Request to reflect the mapping selection?
     * @return ����һ��Containerʵ����������һ��Wrapperʵ��
     */
    @Override
    public Container map(Request request, boolean update) {

        /**
         * �ӿͻ�������Request�����н�����servlet path : /ModernServlet
         */
        // �鱾��Ĵ���
//        String contextPath = ((HttpServletRequest)request.getRequest()).getContextPath();
//        String requestURI = ((HttpRequest)request.getRequest()).getDecodedRequestURI();
//        String relativeURI = requestURI.substring(contextPath.length());

        /**
         * ����Ϊ�˷��㣬������Chap4������client's http url�н�ȡservletName�ķ�����
         * request.getRequestURI()Ϊ�� /servlet/ModernServlet  ��ȡ���Ϊ��/ModernServlet
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
