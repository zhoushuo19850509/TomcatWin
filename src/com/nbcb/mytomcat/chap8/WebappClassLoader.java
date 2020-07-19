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
     * servlet要引用的jar包的路径：
     * WEB-INF/lib
     */
    protected String jarPath = null;


    /**
     * constructor
     * 按照之前SimpleLoader的方式，初始化一个URLClassLoader实例
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
     * 这个Map，保存的是所有之前加载过的servlet class实例
     * 缓存起来保存在这个map中，后续用到的时候，可以快速加载
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
     * 这个方法主要是实现Reloader接口的modified()方法
     * 主要功能就是遍历repository下各个servlet class是否有修改
     * 一旦修改，就返回true
     *
     * 参考：org.apache.catalina.loader.WebappClassLoader.modified()方法
     * @return
     */
    @Override
    public boolean modified() {




        return false;
    }

    /**
     * 下面这5个方法主要是用于实现LifeCycle接口的
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
