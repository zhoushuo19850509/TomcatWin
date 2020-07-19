package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.loader.Reloader;
import org.apache.catalina.loader.ResourceEntry;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

public class WebappClassLoader extends URLClassLoader implements Reloader , Lifecycle {

    /**
     * servletҪ���õ�jar����·����
     * WEB-INF/lib
     */
    protected String jarPath = null;


    /**
     * constructor
     * ����֮ǰSimpleLoader�ķ�ʽ����ʼ��һ��URLClassLoaderʵ��
     */
    public WebappClassLoader(){
        super(new URL[0]);
    }

    public WebappClassLoader(URL[] urls){
        super(urls);
    }

    public WebappClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }


    /**
     * ���Map�������������֮ǰ���ع���servlet classʵ��
     * �����������������map�У������õ���ʱ�򣬿��Կ��ټ���
     */
    protected Map<String ,ResourceEntry> resourceEntries = new HashMap<String, ResourceEntry>();



    @Override
    public void addRepository(String repository) {
        try {
            repository = (new URL("file",null,
                    repository + File.separator)).toString();
            URLStreamHandler streamHandler = null;
            URL url = new URL(null, repository, streamHandler);
            super.addURL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    /**
     * ���������Ҫ��ʵ��Reloader�ӿڵ�modified()����
     * ��Ҫ���ܾ��Ǳ���repository�¸���servlet class�Ƿ����޸�
     * һ���޸ģ��ͷ���true
     *
     * �ο���org.apache.catalina.loader.WebappClassLoader.modified()����
     * @return
     */
    @Override
    public boolean modified() {




        return false;
    }

    /**
     * ������5��������Ҫ������ʵ��LifeCycle�ӿڵ�
     * @param listener The listener to add
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

    }

    @Override
    public void stop() throws LifecycleException {

    }


    /**
     * getter()/setter() of jar path
     * @return
     */
    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }
}
