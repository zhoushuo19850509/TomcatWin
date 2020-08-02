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
     * Ҫ���ص�WebappClassLoader�������
     * ��Ȼ������Ҳ���Զ��������Լ���Loader��
     * ������Ҫע����ǣ������Լ������Loader��Ҫ�̳�WebappClassLoader
     * ���������ڵ���createClassLoader()��������ʵ����ʱ��Ų����쳣
     */
    private String loaderClass = "com.nbcb.mytomcat.chap8.WebappClassLoader";
    String parentClassLoader = null;

    Container container = null;


    /**
     * ��ǰWebappLoader����첽�߳�
     */
    private Thread thread = null;

    protected LifecycleSupport lifecycle = new LifecycleSupport(this);

    /**
     * ����WebappClassLoader������ִ��ʵ�ʵ�servlet load����
     * ��Ҫע����ǣ�ֻ��getClassLoader()��û��setClassLoader()
     *
     * this.classLoader����createClassLoader()�д�����
     * ��createClassLoader()��WebappLoader lifecycle start()�е��õ�
     * ����֪���˰ɣ�һ��WebappLoader����������ȡ��this.classLoader
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
     * ���list�������repository
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
     * �첽�̣߳�����reload servlet class
     */
    @Override
    public void run() {
        while(!threadDone){

            /**
             * reload�߳�ÿ��һ��ʱ�䣬��reloadһ��
             */
            threadSleep();

            if(!started){
                break;
            }

            /**
             * ���servlet��û���޸ģ����������reload
             */
            System.out.println("the result of lastModified check : " + classLoader.modified());
            if(!classLoader.modified()){
                continue;
            }

            /**
             * һ������servlet���޸ģ�����֪ͨcontext������Ӧ
             * ��ʵ��������load servlet
             */
            notifyContext();
            break;
        }

    }


    /**
     * ÿ������ʱ�䣬ȥ���һ��servlet class�Ƿ�modified
     * Ĭ����15�룬����ͨ�������ļ��޸�
     */
    private int checkInterVal = 10;

    /**
     * reload �߳�����һ���
     */
    private void threadSleep(){
        try {
            Thread.sleep(checkInterVal * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * WebappLoader���첽�̷߳���servlet�����޸ģ���Ҫreload
     * �͵������������֪ͨcontext�����¼���servlet��
     */
    private void notifyContext(){
        /**
         * ��ô֪ͨcontext reload�أ�
         * ���ǵ����ڲ��࣬Ȼ������һ���������߳�
         * ������߳�ִ��reload����
         */
        WebappContextNotifier notifier = new WebappContextNotifier();
        (new Thread(notifier)).start();
    }

    /**
     * �ڲ���
     * ������ڲ��࣬����һ���������̣߳�����context reload
     */
    protected class WebappContextNotifier implements Runnable{
        @Override
        public void run() {
            ((Context)container).reload();
        }
    }

    /**
     * ���˽�з�������Ҫ��ͨ������ķ�ʽ������WebappClassLoaderʵ������
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
     * ����������Ǵ���֮ǰ��SimpleLoader
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
             * respository: ����һ��url��ַ��
             * ��ַ��ʽ��Ҫ�ǰ���fileЭ�����WEB_ROOT�±���õ�servlet class�����磺
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
     * ����5����������ʵ��Lifecycle�ӿڵ�
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
     * start()����Ҫ����Щ�����أ�
     * 1.����һ��WebappClassLoaderʵ��
     * 2.����repository
     * 3.������ǰWebappLoader���첽�̣߳����servlet�Ƿ���reload
     * @throws LifecycleException
     */
    @Override
    public void start() throws LifecycleException {

        lifecycle.fireLifecycleEvent(START_EVENT,null);
        started = true;


        /**
         * ����Ϊ�˵��ԣ��ȳ��Դ���һ��SimpleLoader(�ο�chap5/SimpleLoader.java)
         */
//        classLoader = createSimpleClassLoader();

        /**
         * ���ñ��ص�˽�з���������һ��WebappClassLoaderʵ��
         */
        classLoader = createClassLoader();
        System.out.println("finish create class loader");

        /**
         * ����repository
         */
        setRepositories();
        System.out.println("finish set repository");

        setClassPath();

        setPermissions();

        /**
         * ��Loader������ʱ���Ȼ�ȡһ��WEB-INF/classes������class���lastModified�ֶ�
         * ���ں���reload()
         */
        classLoader.setLastModifieds();


        /**
         * ����Loader
         * ��ע����ע��˳�򣬱�����setRepositories()��setPermissions()
         * Ȼ��������Loader
         */
        if(classLoader instanceof Lifecycle){
            ((Lifecycle)classLoader).start();
        }

        /**
         * ������ǰWebappLoader���첽�̣߳���ʼ���servlet�Ƿ���reload
         */
        threadStart();


        System.out.println("WebappLoader started! The repository of the new web app class loader is : " +
                classLoader.getURLs()[0]);

    }


    /**
     * stop()������Ҫ����Щ�����أ�
     * 1.ֹͣClassLoader
     * ������ؼ��Ķ�������Ȼ��ֹͣWebappClassLoader��
     * 2.ֹͣ��ǰLoader�첽�߳�
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
     * ���������Ҫ������Repository
     * ��ν��Repository����ʵ����
     * 1.WEB-INF/classes
     *   servlet class���ڵ�Ŀ¼
     * 2.WEB-INF/lib
     *   servlet��Ҫ�õ���jar��Ŀ¼
     */
    private void setRepositories(){
        String classRepository = System.getProperty("user.dir") + "/WEB-INF/classes";

        this.classLoader.addRepository(classRepository);

        String jarPath = "/WEB-INF/lib";
        this.classLoader.setJarPath(jarPath);
    }

    /**
     * �����������������context���ԣ���Ҫ������JSP������صĹ��ܣ�������ʱ��չ��
     */
    private void setClassPath(){
        // TODO
    }

    /**
     * ����Servlet���ܹ����ʵ�Ȩ��
     * ��Ҫ������servlet�����ܷ���ĳ����Ŀ¼�����磺
     * WEB-INF/classes
     * WEB-INF/lib
     */
    public void setPermissions(){

    }



    /**
     * ������ǰWebappLoader���첽�߳�
     * �����̨�߳����������أ���Ȼ������reload���ܣ����ڼ��WEB-INFĿ¼�µ��ļ��Ƿ��и��¡�
     */
    private void threadStart(){
        if(thread != null){
            return;
        }
        String threadName = "WebappLoader[" + container.getName()+ "]";
        thread = new Thread(this,threadName);
        thread.setDaemon(true);  // ����Ϊ��̨�߳�
        thread.start();

    }

    /**
     * ֹͣ��ǰWebappLoader���첽�߳�
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
