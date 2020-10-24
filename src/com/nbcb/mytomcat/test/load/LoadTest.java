package com.nbcb.mytomcat.test.load;



import com.nbcb.mytomcat.chap3.ModernServlet;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 这个Test类，主要是比对了在for循环条件下，各种场景创建实例的耗时情况
 * 性能比对：
 * 提前创建好class类循环创建对象 = 直接new对象 > 反射方式创建对象 > URLClassLoader
 *
 * 但是性能差距不太大，在ms级别内
 * 原因是多方面的：
 * 1.我们要加载的类比较简单；
 * 2.没有并发；
 * 3.要加载的类没有引用其他的类或者jar包
 *
 * 我相信，一旦要加载的servlet变得复杂、客户端并发调用、要引用其他类或者Jar包，
 * 差距会进一步拉大。差距主要是在对class文件的解析上。
 */
public class LoadTest {
    public static void main(String[] args) {
        System.out.println("start");
        testcase4();
    }

    /**
     * 测试案例1 for循环比对class的效率
     * 加载方式为通过反射创建对象实例
     * 1000000循环耗时600ms不到一点
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
     * new object的方式创建对象实例
     * 1000000次，耗时17ms
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
     * 通过URLClassLoader的方式加载实例，原理和反射差不多，但是性能要稍微低一点
     * 1000000循环耗时875ms
     */
    public static void testcase3(){

        URL[] urls = new URL[1];;
        String className = "com.nbcb.mytomcat.test.load.Employee";
        Class myClass = null;

        try {

            /**
             * 先初始化ClassLoader
             */
            urls[0] = new URL("file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/");
            URLClassLoader classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());

            /**
             * 然后通过ClassLoader，循环加载我们的CLass，并且实例化
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
     * 最后我们采用class load的方式
     * 提前把class类准备好，
     * 循环1000000次创建实例，耗时15ms
     */
    public static void testcase4(){

        URL[] urls = new URL[1];;
        String className = "com.nbcb.mytomcat.test.load.Employee";
        Class myClass = null;

        try {

            /**
             * 先初始化ClassLoader
             */
            urls[0] = new URL("file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/");
            URLClassLoader classLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());

            /**
             * 然后提前创建好Class类
             */
            myClass = classLoader.loadClass(className);

            /**
             * 最后拿我们之前创建的Class类实例化我们需要的对象
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
