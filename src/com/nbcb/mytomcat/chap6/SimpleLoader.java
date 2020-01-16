package com.nbcb.mytomcat.chap6;

import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * �������������(����)Servlet��
 */
public class SimpleLoader implements Loader, Pipeline,Lifecycle {

    /**
     * the classloader of Loader
     */
    private URLClassLoader classLoader;




    /**
     * constructor of SimpleLoader
     * ��ʼ����Loader���󣬹�Wrapperʹ��������Servlet
     * ����߼��Ǵ�Chap4/SimpleContainer.java����������
     */
    public SimpleLoader(){
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);

            String repository = null;
            repository = (new URL("file",null,
                    classPath.getCanonicalPath() + File.separator)).toString();

            /**
             * respository: ����һ��url��ַ��
             * ��ַ��ʽ��Ҫ�ǰ���fileЭ�����WEB_ROOT�±���õ�servlet class�����磺
             * file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/
             */
//            System.out.println("repository: " + repository);
            urls[0] = new URL(null,repository,streamHandler);
            this.classLoader = new URLClassLoader(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public DefaultContext getDefaultContext() {
        return null;
    }

    @Override
    public void setDefaultContext(DefaultContext defaultContext) {

    }

    @Override
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean delegate) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean reloadable) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void addRepository(String repository) {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

    /**
     * ������Щgetter()/setter()����ʵ����Pipeline interface
     * @return
     */
    @Override
    public Valve getBasic() {
        return null;
    }

    @Override
    public void setBasic(Valve valve) {

    }

    @Override
    public void addValve(Valve valve) {

    }

    @Override
    public Valve[] getValves() {
        return new Valve[0];
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

    }

    @Override
    public void removeValve(Valve valve) {

    }

    /**
     * ������5��������Ҫ��ʵ����Lifecycle�ӿڵķ���
     */
    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("starting SimpleLoader by Lifecycle");
    }

    @Override
    public void stop() throws LifecycleException {

    }
}
