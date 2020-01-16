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
 * ���SimpleWrapper��������ִ�е���Servlet
 * ��֮ǰִ�е���Servlet��ͬ
 * (chap4��ִ��Servlet�Ƚϼ򵥣����ǵ���SimpleContainer.invoke()ִ��Servlet)��
 * SimpleWrapperִ��Servlet����רҵ��
 * 1.ͨ��ר�ŵ�Loader��������Servlet class(������Wrapper�ӿڵ�getLoader()/setLoader()����)
 * 2.ͨ��Pipeline��ˮ�ߵķ�ʽ���ø�����ˮ�߽ڵ�
 *   (������Pipeline�ӿڵ�setBasic()����������SimpleWrapperValve����Ϊ��ˮ�߽ڵ�֮һ��ִ��Servlet��
 *   ��Ȼ��������������ˮ�߽ڵ�)
 */
public class SimpleWrapper implements Wrapper,Pipeline,Lifecycle {

    /**
     * ��ǰSimpleWrapperҪ���ص�Servletʵ��������
     * (������ʵӦ�ð���Servlet��package·����)
     */
    private String servletClass;

    /**
     * ��ǰSimpleWrapperҪ��ʼ����Servletʵ��
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
     * һ����ʼ�������ϸ�pipeline���ú�ר��invoke Servlet��Valve
     * ��ˣ�һ����˵ÿ��SimpleWrapper����һ��Ĭ�ϵĻ���Valve������invoke Servlet
     */
    public SimpleWrapper(){
        /**
         * set the basic valve of the pipeline
         */
        pipeline.setBasic(new SimpleWrapperValve());
    }



    /**
     * ��������getter()/setter()����
     * ʵ����Wrapper�ӿڵ�getLoader()����
     * �����ǰWrapper��Loaderʵ���������Ϸ���
     * ������Գ��Ի�ȡ��Container��Loaderʵ��
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
     * �������ʵ����Wrapper�ӿ�
     * ��Ҫ�Ǹ���SimpleLoader������һ��Servletʵ��
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
     * ���������Ҫ������SimpleLoader������Servletʵ��
     * @return
     */
    private Servlet loadServlet() throws ServletException {

        /**
         * servlet�Ѿ���ʼ�����ˣ���ֱ�ӷ�����
         */
        if(instance != null){
            return instance;
        }

        String servletName = getServletClass();
        /**
         * ���δָ��servletName�����ϱ���
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
             * ��ȡ��servletʵ��֮���������servlet��ʼ��һ��
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
     * addValve()�������ʵ����Pipeline�ӿ�
     * ��Ҫ�ǰѸ���valve���뵽pipeline�У��������԰�˳��һ����ִ��
     * @param valve Valve to be added
     *
     */
    @Override
    public void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    /**
     * ���invoke()����ʵ����Wrapper�ӿڵ�invoke()����
     * ���������Ȼ�����container�ĺ��ģ�����pipeline���������pipeline�ĸ���valve
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
     * ������5��������Ҫ��ʵ����Lifecycle�ӿڵķ���
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
