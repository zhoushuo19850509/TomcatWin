package com.nbcb.mytomcat.test.load;



import com.nbcb.mytomcat.chap3.ModernServlet;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * ���Test�࣬��Ҫ�Ǳȶ�����forѭ�������£����ֳ�������ʵ���ĺ�ʱ���
 * ���ܱȶԣ�
 * ��ǰ������class��ѭ���������� = ֱ��new���� > ���䷽ʽ�������� > URLClassLoader
 *
 * �������ܲ�಻̫����ms������
 * ԭ���Ƕ෽��ģ�
 * 1.����Ҫ���ص���Ƚϼ򵥣�
 * 2.û�в�����
 * 3.Ҫ���ص���û�����������������jar��
 *
 * �����ţ�һ��Ҫ���ص�servlet��ø��ӡ��ͻ��˲������á�Ҫ�������������Jar����
 * �����һ�����󡣲����Ҫ���ڶ�class�ļ��Ľ����ϡ�
 */
public class LoadTest {
    public static void main(String[] args) {
        System.out.println("start");
        testcase4();
    }

    /**
     * ���԰���1 forѭ���ȶ�class��Ч��
     * ���ط�ʽΪͨ�����䴴������ʵ��
     * 1000000ѭ����ʱ600ms����һ��
     */
    public static void testcase1(){
        int loopCount = 1000000;
        String name = "com.nbcb.mytomcat.test.load.Employee";
        try {
            long start = System.currentTimeMillis();
            for (int i = 0; i < loopCount; i++) {
                Class cl = Class.forName(name);
                Object obj = cl.newInstance();
            }
            long end = System.currentTimeMillis();
            System.out.println("time cost: " + ( end - start ) + " ms");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * new object�ķ�ʽ��������ʵ��
     * 1000000�Σ���ʱ17ms
     */
    public static void testcase2(){
        int loopCount = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            ModernServlet modernServlet = new ModernServlet();
        }
        long end = System.currentTimeMillis();
        System.out.println("time cost: " + ( end - start ) + " ms");
    }


    /**
     * ͨ��URLClassLoader�ķ�ʽ����ʵ����ԭ��ͷ����࣬��������Ҫ��΢��һ��
     * 1000000ѭ����ʱ875ms
     */
    public static void testcase3(){

        URL[] urls = new URL[1];;
        String className = "com.nbcb.mytomcat.test.load.Employee";
        Class myClass = null;

        try {

            /**
             * �ȳ�ʼ��ClassLoader
             */
            urls[0] = new URL("file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/");
            URLClassLoader classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());

            /**
             * Ȼ��ͨ��ClassLoader��ѭ���������ǵ�CLass������ʵ����
             */
            int loopCount = 1000000;
            long start = System.currentTimeMillis();
            for (int i = 0; i < loopCount; i++) {
                myClass = classLoader.loadClass(className);
//            System.out.println("class resource of the servlet instance: " + myClass.getResource("/"));

                Employee employee = (Employee)myClass.newInstance();
            }
            long end = System.currentTimeMillis();
            System.out.println("time cost: " + ( end - start ) + " ms");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ������ǲ���class load�ķ�ʽ
     * ��ǰ��class��׼���ã�
     * ѭ��1000000�δ���ʵ������ʱ15ms
     */
    public static void testcase4(){

        URL[] urls = new URL[1];;
        String className = "com.nbcb.mytomcat.test.load.Employee";
        Class myClass = null;

        try {

            /**
             * �ȳ�ʼ��ClassLoader
             */
            urls[0] = new URL("file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/");
            URLClassLoader classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());

            /**
             * Ȼ����ǰ������Class��
             */
            myClass = classLoader.loadClass(className);

            /**
             * ���������֮ǰ������Class��ʵ����������Ҫ�Ķ���
             */
            int loopCount = 1000000;
            long start = System.currentTimeMillis();
            for (int i = 0; i < loopCount; i++) {
                Employee employee = (Employee)myClass.newInstance();
            }
            long end = System.currentTimeMillis();
            System.out.println("time cost: " + ( end - start ) + " ms");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


}
