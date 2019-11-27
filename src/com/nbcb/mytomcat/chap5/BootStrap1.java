package com.nbcb.mytomcat.chap5;

import com.nbcb.mytomcat.chap4.HttpConnector;
import org.apache.catalina.*;

import java.io.IOException;

/**
 * the bootstrap of Chap5
 * 为了看懂这个文件，我们要结合Chap5的整体代码架构来看
 * 参考visio架构图：
 * TomcatWin\doc\Tomcat整体架构图.vsd\Chap5 整体框架
 */
public class BootStrap1 {
    public static void main(){
        /**
         * 调用Chap4的HttpConnector启动ServerSocket，处理来自客户端的请求
         */
        HttpConnector connector = new HttpConnector();

        /**
         * 这个例子只调用一个Servlet
         * 所以只要定义一个Wrapper来处理这个单个的Servlet就行了
         */
        Wrapper wrapper = new SimpleWrapper();

        /**
         * 调用我们自己定义的SimpleLoader类，来加载本地的Servlet类
         */
        Loader loader = new SimpleLoader();

        /**
         * new 两个Valve对象
         * 后续我们Wrapper处理Servlet的时候，会调用这两个流水线节点
         */
        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        /**
         * 设置wrapper的Loader
         * 用于加载Servlet实例
         */
        wrapper.setLoader(loader);


        /**
         * 在这里指定要调用的ServletName
         * 疑问：这个ServletName难道不是从request url中获取吗？？
         * 这个后续当然是要优化的
         */
        wrapper.setServletClass("");

        /**
         * 把我们之前定义的这两个Valve设置到wrapper流水线中
         */
        ((Pipeline)wrapper).addValve(valve1);
        ((Pipeline)wrapper).addValve(valve2);

        /**
         * 这个方法很关键，就是把wrapper设置为connector
         * 后续connector解析完客户端http请求之后，就会调用wrapper.invoke()方法
         * 具体请参考chap4/HttpProcessor.process(): container.invoke(request,response);
         */
        connector.setContainer(wrapper);
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
