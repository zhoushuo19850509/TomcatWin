package com.nbcb.mytomcat.chap5;

import com.nbcb.mytomcat.chap4.HttpConnector;
import org.apache.catalina.*;

import java.io.IOException;

public class BootStrap2 {

    public static void main(String[] args){
        /**
         * 调用Chap4的HttpConnector启动ServerSocket，处理来自客户端的请求
         */
        HttpConnector connector = new HttpConnector();

        /**
         * 和BootSrap1不同的是，这个例子需要处理两个Servlet，
         * 分别由两个Wrapper处理，这两个Wrapper挂靠在一个SimpleContext下
         */
        Wrapper wrapper1 = new SimpleWrapper();
        Wrapper wrapper2 = new SimpleWrapper();

        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        /**
         * 调用我们自己定义的SimpleLoader类，来加载本地的Servlet类
         * 然后设置给SimpleContext
         */
        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        /**
         * new 两个Valve对象
         * 后续我们Wrapper处理Servlet的时候，会调用这两个流水线节点
         */
        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        /**
         * 这里要设置一个映射关系
         * 将客户端URL中Servlet相关的地址("/Modern")和Wrapper名称("Modern")映射起来
         */
        context.addServletMapping("/ModernServlet","Modern");
        context.addServletMapping("/PrimitiveServlet","Primitive");

        /**
         * 把我们之前定义的这两个Valve设置到SimpleContext流水线中
         */
        ((Pipeline)context).addValve(valve1);
        ((Pipeline)context).addValve(valve2);

        /**
         * 这个方法很关键，就是把context设置给connector
         * 后续connector解析完客户端http请求之后，就会调用context.invoke()方法
         * 具体请参考chap4/HttpProcessor.process(): container.invoke(request,response);
         */
        connector.setContainer(context);
        try {
            connector.initialize();
            connector.start();

            /**
             * wait until we press any key
             */
            System.in.read();

        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
