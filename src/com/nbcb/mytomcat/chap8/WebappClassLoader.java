package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.util.Constants;
import com.nbcb.mytomcat.util.FileUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebappClassLoader extends URLClassLoader implements Reloader , Lifecycle {

    /**
     * servlet要引用的jar包的路径：
     * WEB-INF/lib
     */
    protected String jarPath = null;



    /**
     * 存放servlet class类的目录
     * /Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes
     */
    String repository = null;


    /**
     * WEB-INF/classes目录下所有class文件的lastModified字段，放在这个List中
     */
    protected List<Long> lastModifieds = null;

    public List<Long> getLastModifieds() {
        return lastModifieds;
    }

    public void setLastModifieds(){

        if(null == lastModifieds){
            lastModifieds = new ArrayList<Long>();
        }
        FileUtil.getClassFileLastModifies(repository, lastModifieds);
    }




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
            this.repository = repository;
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
     *
     * @TODO 目前的版本还只处理了class类，暂时没有处理jar包，后续要加上
     *
     * @return 如果WEB-INF/classes下一旦有class变化，就返回true
     */
    @Override
    public boolean modified() {

        /**
         * 先获取一遍当前WEB-INF/classes下所有class类的lastModified字段
         */
        List<Long> currentLastModifieds = new ArrayList<Long>();
        FileUtil.getClassFileLastModifies(this.repository, currentLastModifieds);
        if(null == currentLastModifieds){
            return true;
        }

        /**
         * 然后和上次记录的内容进行比对
         */
        List<Long> previousLastModifieds = getLastModifieds();
        if(null == previousLastModifieds){
            return true;
        }

        /**
         * 如果class类文件数量不一致，说明servlet类有增减，返回true
         */
        if(currentLastModifieds.size() != previousLastModifieds.size()){
            return true;
        }

        /**
         * 然后遍历当前WEB-INF/classes下所有class类的lastModified字段
         * 和之前记录的classa文件进行比对
         * 只要有一个文件不一致，就返回true
         */
        for (int i = 0; i < currentLastModifieds.size(); i++) {
            Long currentLastModified =  currentLastModifieds.get(i);
            Long previousLastModified = previousLastModifieds.get(i);
            if(currentLastModified.intValue() != previousLastModified.intValue()){
                return true;
            }
        }

        /**
         * 用于调试，打印currentLastModifieds、previousLastModifieds
         */
        System.out.println("start print currentLastModifieds");
        for(Long currentLastModified : currentLastModifieds ){
            System.out.println(currentLastModified);
        }
        System.out.println("start print previousLastModifieds");
        for(Long previousLastModified : previousLastModifieds ){
            System.out.println(previousLastModified);
        }

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


    public String getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        return "WebappClassLoader{" +
                "jarPath='" + jarPath + '\'' +
                ", repository='" + repository + '\'' +
                ", lastModifieds=" + lastModifieds +
                ", resourceEntries=" + resourceEntries +
                '}';
    }

    public static void main(String[] args) {
        File file = new File("/Users/zhoushuo/Documents/tmp/a.txt");
        long lastModified = file.lastModified();

        // 1595745165000
        // 1595745261000
//        System.out.println(lastModified);


        List<Long> lastModiyiedDates = new ArrayList<Long>();
        FileUtil.getClassFileLastModifies("/Users/zhoushuo/Documents/tmp",lastModiyiedDates);
//        for(long lastModifiedDate : lastModiyiedDates){
//            System.out.println(lastModified);
//        }
        for (int i = 0; i < lastModiyiedDates.size(); i++) {
            Long aLong =  lastModiyiedDates.get(i);
            System.out.println(i + " : " + aLong);
        }

    }





}
