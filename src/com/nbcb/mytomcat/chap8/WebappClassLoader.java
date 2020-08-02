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
     * servletҪ���õ�jar����·����
     * WEB-INF/lib
     */
    protected String jarPath = null;



    /**
     * ���servlet class���Ŀ¼
     * /Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes
     */
    String repository = null;


    /**
     * WEB-INF/classesĿ¼������class�ļ���lastModified�ֶΣ��������List��
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
     * ���������Ҫ��ʵ��Reloader�ӿڵ�modified()����
     * ��Ҫ���ܾ��Ǳ���repository�¸���servlet class�Ƿ����޸�
     * һ���޸ģ��ͷ���true
     *
     * �ο���org.apache.catalina.loader.WebappClassLoader.modified()����
     *
     * @TODO Ŀǰ�İ汾��ֻ������class�࣬��ʱû�д���jar��������Ҫ����
     *
     * @return ���WEB-INF/classes��һ����class�仯���ͷ���true
     */
    @Override
    public boolean modified() {

        /**
         * �Ȼ�ȡһ�鵱ǰWEB-INF/classes������class���lastModified�ֶ�
         */
        List<Long> currentLastModifieds = new ArrayList<Long>();
        FileUtil.getClassFileLastModifies(this.repository, currentLastModifieds);
        if(null == currentLastModifieds){
            return true;
        }

        /**
         * Ȼ����ϴμ�¼�����ݽ��бȶ�
         */
        List<Long> previousLastModifieds = getLastModifieds();
        if(null == previousLastModifieds){
            return true;
        }

        /**
         * ���class���ļ�������һ�£�˵��servlet��������������true
         */
        if(currentLastModifieds.size() != previousLastModifieds.size()){
            return true;
        }

        /**
         * Ȼ�������ǰWEB-INF/classes������class���lastModified�ֶ�
         * ��֮ǰ��¼��classa�ļ����бȶ�
         * ֻҪ��һ���ļ���һ�£��ͷ���true
         */
        for (int i = 0; i < currentLastModifieds.size(); i++) {
            Long currentLastModified =  currentLastModifieds.get(i);
            Long previousLastModified = previousLastModifieds.get(i);
            if(currentLastModified.intValue() != previousLastModified.intValue()){
                return true;
            }
        }

        /**
         * ���ڵ��ԣ���ӡcurrentLastModifieds��previousLastModifieds
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
