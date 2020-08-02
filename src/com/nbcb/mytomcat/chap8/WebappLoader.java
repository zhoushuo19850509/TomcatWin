package com.nbcb.mytomcat.chap8;


import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;



public class WebappLoader implements Loader, Runnable, Lifecycle {


    /**
     *
     */
    private WebappClassLoader classLoader = null;

    /**
     * 要加载的WebappClassLoader类的名称
     * 当然，我们也可以定义我们自己的Loader类
     * 但是需要注意的是，我们自己定义的Loader类要继承WebappClassLoader
     * 这样我们在调用createClassLoader()方法创建实例的时候才不会异常
     */
    private String loaderClass = "com.nbcb.mytomcat.chap8.WebappClassLoader";
    String parentClassLoader = null;

    Container container = null;


    /**
     * 当前WebappLoader类的异步线程
     */
    private Thread thread = null;

    protected LifecycleSupport lifecycle = new LifecycleSupport(this);

    /**
     * 返回WebappClassLoader，用于执行实际的servlet load任务
     * 需要注意的是，只有getClassLoader()，没有setClassLoader()
     *
     * this.classLoader是在createClassLoader()中创建的
     * 而createClassLoader()是WebappLoader lifecycle start()中调用的
     * 所以知道了吧，一旦WebappLoader启动，就能取到this.classLoader
     */
    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;

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

    /**
     * 这个list保存的是repository
     */
    private List<String> repositories = new ArrayList<String>();

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


    private boolean threadDone = false;
    private boolean started = false;

    /**
     * 异步线程，用于reload servlet class
     */
    @Override
    public void run() {
        while(!threadDone){

            /**
             * reload线程每隔一段时间，才reload一下
             */
            threadSleep();

            if(!started){
                break;
            }

            /**
             * 如果servlet类没有修改，就跳过这次reload
             */
            System.out.println("the result of lastModified check : " + classLoader.modified());
            if(!classLoader.modified()){
                continue;
            }

            /**
             * 一旦发现servlet有修改，马上通知context作出反应
             * 其实就是重新load servlet
             */
            notifyContext();
            break;
        }

    }


    /**
     * 每隔多少时间，去检查一下servlet class是否modified
     * 默认是15秒，可以通过配置文件修改
     */
    private int checkInterVal = 10;

    /**
     * reload 线程休眠一会儿
     */
    private void threadSleep(){
        try {
            Thread.sleep(checkInterVal * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * WebappLoader的异步线程发现servlet类有修改，需要reload
     * 就调用这个方法，通知context，重新加载servlet类
     */
    private void notifyContext(){
        /**
         * 怎么通知context reload呢？
         * 就是调用内部类，然后启动一个单独的线程
         * 由这个线程执行reload动作
         */
        WebappContextNotifier notifier = new WebappContextNotifier();
        (new Thread(notifier)).start();
    }

    /**
     * 内部类
     * 由这个内部类，启动一个单独的线程，出发context reload
     */
    protected class WebappContextNotifier implements Runnable{
        @Override
        public void run() {
            ((Context)container).reload();
        }
    }

    /**
     * 这个私有方法，主要是通过反射的方式，创建WebappClassLoader实例对象
     * @return
     */
    private WebappClassLoader createClassLoader(){
        WebappClassLoader classLoader = null;

        try {
            Class clazz = Class.forName(loaderClass);
            if(null == parentClassLoader){
                classLoader = (WebappClassLoader) clazz.newInstance();
            }else{
                // TODO
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return classLoader;
    }

    /**
     * 这个方法还是创建之前的SimpleLoader
     * @return
     */
    private WebappClassLoader createSimpleClassLoader(){
        URL[] urls = null;
        try {
            urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);

            String repository = null;
            repository = (new URL("file",null,
                    classPath.getCanonicalPath() + File.separator)).toString();

            /**
             * respository: 这是一个url地址，
             * 地址形式主要是按照file协议访问WEB_ROOT下编译好的servlet class，比如：
             * file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/
             */

//            System.out.println("repository: " + repository);
            urls[0] = new URL(null,repository,streamHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WebappClassLoader(urls);
    }





    /**
     * 以下5个方法，是实现Lifecycle接口的
     *
     * @param listener The listener to add
     */
    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }


    /**
     * start()方法要做哪些事情呢？
     * 1.创建一个WebappClassLoader实例
     * 2.配置repository
     * 3.启动当前WebappLoader的异步线程，检测servlet是否有reload
     * @throws LifecycleException
     */
    @Override
    public void start() throws LifecycleException {

        lifecycle.fireLifecycleEvent(START_EVENT,null);
        started = true;


        /**
         * 这里为了调试，先尝试创建一个SimpleLoader(参考chap5/SimpleLoader.java)
         */
//        classLoader = createSimpleClassLoader();

        /**
         * 调用本地的私有方法，创建一个WebappClassLoader实例
         */
        classLoader = createClassLoader();
        System.out.println("finish create class loader");

        /**
         * 配置repository
         */
        setRepositories();
        System.out.println("finish set repository");

        setClassPath();

        setPermissions();

        /**
         * 在Loader启动的时候，先获取一遍WEB-INF/classes下所有class类的lastModified字段
         * 用于后续reload()
         */
        classLoader.setLastModifieds();


        /**
         * 启动Loader
         * 备注：请注意顺序，必须先setRepositories()、setPermissions()
         * 然后再启动Loader
         */
        if(classLoader instanceof Lifecycle){
            ((Lifecycle)classLoader).start();
        }

        /**
         * 启动当前WebappLoader的异步线程，开始检测servlet是否有reload
         */
        threadStart();


        System.out.println("WebappLoader started! The repository of the new web app class loader is : " +
                classLoader.getURLs()[0]);

    }


    /**
     * stop()方法中要做哪些事情呢？
     * 1.停止ClassLoader
     * 其中最关键的动作，当然是停止WebappClassLoader了
     * 2.停止当前Loader异步线程
     * @throws LifecycleException
     */
    @Override
    public void stop() throws LifecycleException {

        lifecycle.fireLifecycleEvent(STOP_EVENT,null);

        started = false;

        threadStop();

        if(classLoader instanceof Lifecycle){
            ((Lifecycle)classLoader).stop();
        }
        classLoader = null;

    }

    /**
     * 这个方法主要是设置Repository
     * 所谓的Repository，其实就是
     * 1.WEB-INF/classes
     *   servlet class所在的目录
     * 2.WEB-INF/lib
     *   servlet需要用到的jar包目录
     */
    private void setRepositories(){
        String classRepository = System.getProperty("user.dir") + "/WEB-INF/classes";

        this.classLoader.addRepository(classRepository);

        String jarPath = "/WEB-INF/lib";
        this.classLoader.setJarPath(jarPath);
    }

    /**
     * 这个方法好像是设置context属性，主要是用于JSP编译相关的功能，这里暂时不展开
     */
    private void setClassPath(){
        // TODO
    }

    /**
     * 设置Servlet类能够访问的权限
     * 主要是限制servlet类智能访问某几个目录，比如：
     * WEB-INF/classes
     * WEB-INF/lib
     */
    public void setPermissions(){

    }



    /**
     * 启动当前WebappLoader的异步线程
     * 这个后台线程用来干嘛呢？当然是用于reload功能：定期检测WEB-INF目录下的文件是否有更新。
     */
    private void threadStart(){
        if(thread != null){
            return;
        }
        String threadName = "WebappLoader[" + container.getName()+ "]";
        thread = new Thread(this,threadName);
        thread.setDaemon(true);  // 设置为后台线程
        thread.start();

    }

    /**
     * 停止当前WebappLoader的异步线程
     */
    private void threadStop(){
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
    }


}
