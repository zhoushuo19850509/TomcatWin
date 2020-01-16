package com.nbcb.mytomcat.chap6;


import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;

import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * 这个SimpleWrapper就是用来执行单个Servlet
 * 和之前执行单个Servlet不同
 * (chap4中执行Servlet比较简单，就是调用SimpleContainer.invoke()执行Servlet)，
 * SimpleWrapper执行Servlet更加专业：
 * 1.通过专门的Loader类来加载Servlet class(重载了Wrapper接口的getLoader()/setLoader()方法)
 * 2.通过Pipeline流水线的方式调用各个流水线节点
 *   (重载了Pipeline接口的setBasic()方法，设置SimpleWrapperValve类作为流水线节点之一，执行Servlet，
 *   当然还重载了其他流水线节点)
 */
public class SimpleWrapper implements Wrapper,Pipeline,Lifecycle {

    /**
     * 当前SimpleWrapper要加载的Servlet实例的名称
     * (我想其实应该包含Servlet的package路径吧)
     */
    private String servletClass;

    /**
     * 当前SimpleWrapper要初始化的Servlet实例
     */
    private Servlet instance;

    /**
     * the pipeline of the wrapper
     */
    private SimplePipeline pipeline = new SimplePipeline(this);

    /**
     * The (Servlet) loader of this Wrapper
     */
    private Loader  loader;

    /**
     * The parent Container of this Wrapper(Container)
     */
    protected Container parent = null;

    /**
     * Wrapper's name
     */
    private String name;

    protected LifecycleSupport lifecycle = new LifecycleSupport(this);


    /**
     * constructor
     * 一旦初始化，马上给pipeline设置好专门invoke Servlet的Valve
     * 因此，一般来说每个SimpleWrapper都有一个默认的基本Valve，用于invoke Servlet
     */
    public SimpleWrapper(){
        /**
         * set the basic valve of the pipeline
         */
        pipeline.setBasic(new SimpleWrapperValve());
    }



    /**
     * 以下两个getter()/setter()方法
     * 实现了Wrapper接口的getLoader()方法
     * 如果当前Wrapper有Loader实例，就马上返回
     * 否则可以尝试获取父Container的Loader实例
     * @return
     */
    @Override
    public Loader getLoader() {

        if(this.loader != null){
            return loader;
        }
        if(this.parent != null){
            return parent.getLoader();
        }
        return null;
    }


    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    /**
     * 这个方法实现了Wrapper接口
     * 主要是根据SimpleLoader，返回一个Servlet实例
     * @return
     * @throws ServletException
     */
    @Override
    public Servlet allocate() throws ServletException {

        if(null == instance){
            instance = loadServlet();
        }
        return instance;
    }

    /**
     * 这个方法主要是利用SimpleLoader来加载Servlet实例
     * @return
     */
    private Servlet loadServlet() throws ServletException {

        /**
         * servlet已经初始化过了，就直接返回了
         */
        if(instance != null){
            return instance;
        }

        String servletName = getServletClass();
        /**
         * 如果未指定servletName，马上报错
         */
        if(null == servletName){
            throw new ServletException("Servlet name not specified!");
        }

        Loader loader = getLoader();
        if(null == loader){
            throw new ServletException("No loader!");
        }

        ClassLoader classLoader = loader.getClassLoader();

        Class myClass = null;
        try {
            myClass =    classLoader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Servlet servlet = null;

        try {
            servlet = (Servlet)myClass.newInstance();

            /**
             * 获取到servlet实例之后，在这里对servlet初始化一下
             */
            servlet.init(null);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return servlet;
    }


    @Override
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {
    }

    /**
     * addValve()这个方法实现了Pipeline接口
     * 主要是把各个valve插入到pipeline中，后续可以按顺序一个个执行
     * @param valve Valve to be added
     *
     */
    @Override
    public void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    /**
     * 这个invoke()方法实现了Wrapper接口的invoke()方法
     * 这个方法显然是这个container的核心：触发pipeline，逐个调用pipeline的各个valve
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request,response);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public long getAvailable() {
        return 0;
    }

    @Override
    public void setAvailable(long available) {

    }

    @Override
    public String getJspFile() {
        return null;
    }

    @Override
    public void setJspFile(String jspFile) {

    }

    @Override
    public int getLoadOnStartup() {
        return 0;
    }

    @Override
    public void setLoadOnStartup(int value) {

    }

    @Override
    public String getRunAs() {
        return null;
    }

    @Override
    public void setRunAs(String runAs) {

    }

    @Override
    public String getServletClass() {
        return servletClass;
    }

    @Override
    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    @Override
    public boolean isUnavailable() {
        return false;
    }

    @Override
    public void addInitParameter(String name, String value) {

    }

    @Override
    public void addInstanceListener(InstanceListener listener) {

    }

    @Override
    public void addSecurityReference(String name, String link) {

    }

    @Override
    public void deallocate(Servlet servlet) throws ServletException {

    }

    @Override
    public String findInitParameter(String name) {
        return null;
    }

    @Override
    public String[] findInitParameters() {
        return new String[0];
    }

    @Override
    public String findSecurityReference(String name) {
        return null;
    }

    @Override
    public String[] findSecurityReferences() {
        return new String[0];
    }

    @Override
    public void load() throws ServletException {

    }

    @Override
    public void removeInitParameter(String name) {

    }

    @Override
    public void removeInstanceListener(InstanceListener listener) {

    }

    @Override
    public void removeSecurityReference(String name) {

    }

    @Override
    public void unavailable(UnavailableException unavailable) {

    }

    @Override
    public void unload() throws ServletException {

    }

    @Override
    public String getInfo() {
        return null;
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
    public Container getParent() {
        return this.parent;
    }


    @Override
    public void setParent(Container container) {
        this.parent = container;
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
    public Valve[] getValves() {
        return new Valve[0];
    }

    @Override
    public void removeValve(Valve valve) {

    }

    @Override
    public Container map(Request request, boolean update) {
        return null;
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

    /**
     * 下面这5个方法主要是实现了Lifecycle接口的方法
     */
    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
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


        lifecycle.fireLifecycleEvent(BEFORE_START_EVENT,null);
        System.out.println("starting SimpleWrapper "+ name);
        lifecycle.fireLifecycleEvent(START_EVENT,null);
    }

    @Override
    public void stop() throws LifecycleException {
        lifecycle.fireLifecycleEvent(BEFORE_STOP_EVENT,null);
        lifecycle.fireLifecycleEvent(STOP_EVENT,null);
    }
}
