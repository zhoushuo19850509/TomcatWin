package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.util.FileUtil;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.loader.Reloader;
import org.apache.catalina.loader.ResourceEntry;

import java.io.File;
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

    /**
     * 简易版的class cache
     * 只保留了servlet对应的Class类对象
     */
    protected Map<String ,Class> resourceEntriesSimple = new HashMap<String ,Class>();


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


        printLoadCache();

        return false;
    }

    /**
     * 这个方法用于打印当前loader cache中，缓存了多少Class对象
     */
    public void printLoadCache(){
        System.out.println("start printLoadCache: ");
        for(String key : resourceEntriesSimple.keySet()){
            System.out.println(key + ":" + resourceEntriesSimple.get(key));
        }
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
     * getter/setter() of lastModifieds
     * @return
     */
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

    /**
     * 重载java.lang.ClassLoader.loadClass(String servletName)方法
     * 这里我们会加入load cache相关的逻辑
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    public Class loadClass(String name) throws ClassNotFoundException {

        /**
         * 先打印一下servlet的名称
         * 比如ModernServlet,这个名称显然是不够完善的
         * 我们需要的是com.nbcb.mytomcat.MordernServlet这样完整的servlet相对路径
         * 这样才能根据servlet路径获取对应的servlet Class对象
         * 这就涉及在配置文件web.xml中定义servlet name和servlet相对路径的映射关系了。
         * 这里为了方便，就先暂时用ModernServlet这个名称把
         * 后续等搞定web.xml解析模块之后，再回过头来优化
         */
        System.out.println("servlet name: " + name);


        Class myClass = null;

        /**
         * 先尝试从Map缓存中看看，这个servlet之前是否已经加载过了
         */
        myClass = findLoadedClass0(name);

        if(null != myClass){
            return myClass;
        }

        /**
         * 接下来，尝试调用JVM的Loader，加载servlet类
         * 防止我们自己应用包中的类覆盖JVM自带的类，造成安全隐患
         */


        /**
         * 如果代码流转到了这里，说明这个servlet类之前没有加载过
         * 那么需要我们从我们自己应用包中加载servlet类
         * 但是，在加载正式的servlet类之前，先要用security manager检查一下这个servlet类的安全性
         * 至少保证我们要加载的servlet类是在我们自己应用的范围内
         * (只能加载当前应用的WEB-INF目录下的servlet和jar包，否则会有安全隐患)
         */


        /**
         * 最后，可以从我们应用包中获取servlet类对应的Class对象了
         */
        myClass = findClass(name);


        return myClass;
    }


    /**
     * 从缓存中看看，之前是否加载过这个servlet
     * 其实就是从Map resourceEntries中，根据servlet name获取之前缓存过的class对象
     * @param name
     * @return
     */
    public Class findLoadedClass0(String name){
        Class myClass = resourceEntriesSimple.get(name);
        if(null != myClass){
            System.out.println("load from cache: " + name);
            return myClass;
        }

        return null;
    }


    /**
     * 根据servlet name获取对应servlet类的Class对象，并且放到缓存中
     * 这个类是简化处理的，对标的是官方代码：
     * WebappClassLoader.findResourceInternal()
     * @param name
     * @return
     */
    public Class findClass(String name){

        /**
         * Step1 还是先从缓存中看看，是否有我们要加载的servlet Class对象
         */
        Class myClass = resourceEntriesSimple.get(name);
        if(null != myClass){
            System.out.println("load from cache");
            return myClass;
        }

        /**
         * 尝试从WEB-INF中，获取我们要加载的servlet类
         * 这里的加载方法，参考了之前SimpleLoader的加载方法，
         * 通过URLClassLoader的方法加载Class对象
         */
        String myRepository = null;
        try {
            myRepository = (new URL("file",null,
                    repository + File.separator)).toString();
            URLStreamHandler streamHandler = null;

            URL[] urls = new URL[1];
            urls[0] = new URL(null,myRepository,streamHandler);
            URLClassLoader loader = new URLClassLoader(urls);
            myClass = loader.loadClass(name);


            /**
             * 如果能加载到我们需要的servlet，就把这个加载好的servlet Class对象放到缓存
             */
            if(null != myClass){
                System.out.println("put the Class into cache : " + name);
                resourceEntriesSimple.put(name,myClass);
                return myClass;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
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
