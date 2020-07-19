package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.chap4.HttpConnector;
import com.nbcb.mytomcat.chap6.*;
import org.apache.catalina.*;
import org.apache.catalina.logger.FileLogger;
import com.nbcb.mytomcat.chap8.SimpleContextMapper;

import java.io.IOException;

/**
 * 这个BootStrap主要为了验证我们的Lifecyle功能
 */
public class BootStrap {

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

        LifecycleListener wrapperListener = new SimpleWrapperLifecycleListener();
        ((SimpleWrapper) wrapper1).addLifecycleListener(wrapperListener);
        ((SimpleWrapper) wrapper2).addLifecycleListener(wrapperListener);

        /**
         * Context换成了我们这章新的Context: StandardContext
         */
        Context context = new StandardContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        /**
         * 调用我们自己定义的WebappLoader类，来加载本地的Servlet类
         * 然后设置给SimpleContext
         */
        Loader loader = new WebappLoader();
        context.setLoader(loader);
        loader.setContainer(context);

        Mapper mapper = new com.nbcb.mytomcat.chap8.SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        /**
         * 这里要设置一个映射关系
         * 将客户端URL中Servlet相关的地址("/Modern")和Wrapper名称("Modern")映射起来
         */
        context.addServletMapping("/ModernServlet","Modern");
        context.addServletMapping("/PrimitiveServlet","Primitive");

        /**
         * 定义一个Listener，专门是用于监听SimpleContext的
         */
        LifecycleListener listener = new SimpleContextLifecycleListener();
        ((StandardContext) context).addLifecycleListener(listener);
        /**
         * 这个方法很关键，就是把context设置给connector
         * 后续connector解析完客户端http请求之后，就会调用context.invoke()方法
         * 具体请参考chap4/HttpProcessor.process(): container.invoke(request,response);
         */
        connector.setContainer(context);


        /**
         * 下面开始设置日志对象： FileLogger
         */
        // 首先，设置一下系统参数："catalina.base"
        // FileLogger对象会用到这个值，后续会把日志放到这个路径下
        // 也就是本工程的地址： /Users/zhoushuo/Documents/workspace/TomcatWin
        System.setProperty("catalina.base",System.getProperty("user.dir"));
        FileLogger logger = new FileLogger();

        // 设置日志文件名称
        logger.setPrefix("MyFileLog_");
        logger.setSuffix(".log");

        // 日志文件中是否打印时间戳
        logger.setTimestamp(true);
        // 日志打印在哪个具体的目录下
        logger.setDirectory("webroot");
        context.setLogger(logger);

        logger.log("hello FileLogger");

        try {
            connector.initialize();
            connector.start();


            /**
             * 因为SimpleContext实现了Lifecycle接口
             * 所以可以直接调用start()接口
             */
            ((Lifecycle)context).start();
            /**
             * wait until we press any key
             */
            System.in.read();

            ((Lifecycle)context).stop();

        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
