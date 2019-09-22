package com.nbcb.mytomcat.chap4;


import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.*;

import javax.management.ObjectName;
import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class SimpleContainer implements Container {

    /**
     * invoke()是这个(java) Container做的主要事情
     * 要做的事情和第三章中ServletProcessor.process()类似
     * 就是根据客户端传入的servlet名称
     * 通过反射的方式实例化一个服务端的servlet class，然后处理来自客户端的请求
     * @param request Request to be processed
     * @param response Response to be produced
     *
     * @throws IOException
     * @throws ServletException
     */
    public void invoke(Request request,Response response){
        /**
         * get the servlet name from url: /servlet/PrimitiveServlet
         */
        String servletName = ((HttpServletRequest)request).getRequestURI();
        servletName = servletName.substring(servletName.lastIndexOf("/") + 1);

        URLClassLoader loader = null;
        try {

//            System.out.println("servletName: " + servletName);


            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);

            String repository = (new URL("file",null,
                    classPath.getCanonicalPath() + File.separator)).toString();

            /**
             * respository: 这是一个url地址，
             * 地址形式主要是按照file协议访问WEB_ROOT下编译好的servlet class，比如：
             * file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/
             */
//            System.out.println("repository: " + repository);
            urls[0] = new URL(null,repository,streamHandler);
            loader = new URLClassLoader(urls);

            Class myClass = null;

            try {
                myClass = loader.loadClass(servletName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Servlet servlet = null;

            try {
                servlet = (Servlet)myClass.newInstance();
                servlet.service((HttpServletRequest) request,(HttpServletResponse) response);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public Container map(Request request, boolean update) {
        return null;
    }


    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Loader getLoader() {
        return null;
    }

    @Override
    public void setLoader(Loader loader) {

    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void setLogger(Logger logger) {

    }

    @Override
    public Manager getManager() {
        return null;
    }

    @Override
    public void setManager(Manager manager) {

    }

    @Override
    public Cluster getCluster() {
        return null;
    }

    @Override
    public void setCluster(Cluster cluster) {

    }



    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public Container getParent() {
        return null;
    }

    @Override
    public void setParent(Container container) {

    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {

    }

    @Override
    public Realm getRealm() {
        return null;
    }

    @Override
    public void setRealm(Realm realm) {

    }

    @Override
    public DirContext getResources() {
        return null;
    }

    @Override
    public void setResources(DirContext resources) {

    }


    @Override
    public void addChild(Container child) {

    }

    @Override
    public void addContainerListener(ContainerListener listener) {

    }

    @Override
    public void addMapper(Mapper mapper) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public Container findChild(String name) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }

    @Override
    public ContainerListener[] findContainerListeners() {
        return new ContainerListener[0];
    }

    @Override
    public Mapper findMapper(String protocol) {
        return null;
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }

    @Override
    public void removeChild(Container child) {

    }

    @Override
    public void removeContainerListener(ContainerListener listener) {

    }

    @Override
    public void removeMapper(Mapper mapper) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

}
