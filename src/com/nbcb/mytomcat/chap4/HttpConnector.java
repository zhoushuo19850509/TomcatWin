package com.nbcb.mytomcat.chap4;

import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.*;
import org.apache.catalina.net.ServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

/**
 * 总体来说，HttpConnector的功能和第三章的差不多
 * 就是启动一个ServerSocket对象，监听来自客户端的请求
 * 当然，和第三章年相比，有很多改进：
 * 1.支持多线程的客户端连接(维护了一个线程池)
 * 2.实现了tomcat的Connector接口
 *   当然是在原有Connector的基础上，新增了一些方法。
 *   getContainer()/setContainer()
 *   initialize()  // 初始化webcontainer线程池
 *   start()  //
 * 3.引入了Container的概念：
 *
 *
 */
public class HttpConnector implements Connector,Runnable{

    /**
     * java Container: SimpleContainer
     * Container作为Java容器，处理Servlet请求
     */
    private Container container = null;

    /**
     * whether the server is stopped
     */
    public static boolean stopped = false;

    /**
     * whether the server is started
     */
    private boolean started = true;

    /////////////////////// 线程池相关 start ///////////////////////
    /**
     * 维护一个线程池，处理来自客户端的请求
     * 这个线程池中的对象，就是HttpProcessor
     */
    private Stack processors = new Stack();

    /**
     * 线程池最小线程数量(线程池初始值)
     * 后续要把线程池配置放到配置文件中
     */
    protected int minProcessors = 5;

    /**
     * 线程池最大线程数量
     */
    private int maxProcessors = 20;

    /**
     * 线程池当前实际的线程数量
     * 我们每次new 一个HttpProcessor实例，都会给这个计数加1
     */
    private int currProccessors = 0;

    /////////////////////// 线程池相关 end ///////////////////////


    /**
     * 服务端监听的ServerSocket
     */
    private ServerSocket serverSocket = null;

    /**
     * 这个bufferSize主要是在Connector中指定读取客户端socket的buffer大小
     * 因为客户端往往只能访问connector，无法访问HttpProcessor
     */
    private int bufferSize = 2048;


    /**
     * @方法功能说明
     * 1.启动HttpConnector的主线程
     * 2.初始化线程池，并且保证线程池里的线程数，至少保持在最小连接数以上
     */
    public void start(){

        /**
         * 启动当前的HttpConnector线程
         */
        started = true;
        startThread();

        /**
         * 对线程池进行初始化
         * 直到线程池中HttpProcessor实例的数量达到了minProcessors         *
         */
        while(currProccessors < minProcessors){
            /**
             * 当前线程数超出最大线程数maxProcessors，退出while循环
             * 为啥？那线程池最小连接怎么维持？
             */
            if(maxProcessors > 0 && currProccessors >= maxProcessors){
                break;
            }

            /**
             * 一旦发现当前线程数小于最小线程数minProcessors
             * 就创建新的HttpProcessor实例
             * 就往线程池中塞入一个新的线程
             */
            HttpProcessor processor = newProcessor();
            recycle(processor);
        }
//        System.out.println("A thread is initialized ,the number of " +
//                " current Proccessor is : " + currProccessors);

    }

    /**
     * @方法功能说明
     * HttpConnector的初始化动作
     * 初始化的时候，需要做这些事情：
     * 1.创建Server Socket (参考chap3中的HttpConnector)
     * 2.根据配置的webcontainer大小，初始化一个线程池(chap4新增功能)
     * 3.
     *
     * @待优化
     * 1.生成ServerSocket的过程，交给factory来做(org.apache.catalina.net.ServerSocketFactory)
     *
     * @throws LifecycleException
     */
    @Override
    public void initialize() throws LifecycleException {

        /**
         * 实例化Serversocket，监听来自客户端的请求
         */
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port ,1 , InetAddress.getByName("127.0.0.1"));
            System.out.println("ServerSocket established! Start listening on port : " + port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /**
         * 创建一个线程池，用于并发处理来自客户端的请求
         */
    }



    /**
     * @方法功能说明
     * HttpConnector的主线程方法：
     * 1.启动一个ServerSocket，用来接收来自客户端的socket连接请求
     * 2.然后尝试从线程池中获取一个HttpProcessor资源，处理这个socket请求
     */
    @Override
    public void run() {

        System.out.println("Start the thread of the default connector : " + this.getClass().getName());

        while (!stopped) {
            Socket socket = null;

            /**
             * accept the incoming connection
             */
            try {

//                System.out.println("HttpConnector accept the incoming client");
                socket = serverSocket.accept();


                /**
                 * 设置socket连接超时时间
                 */
                socket.setSoTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT);
                socket.setTcpNoDelay(true);



                /**
                 * 然后从线程池中取出一个HttpProcessor对象，处理客户端请求
                 */
                HttpProcessor processor = createProcess();

                /**
                 * 把processor设置为null主要是为了验证服务端关闭socket连接
                 * 返回异常给客户端的场景
                 */
//                HttpProcessor processor = null;

                /**
                 * 每次分配一个HttpProcessor线程资源之后，都打印一下当前线程池的状态
                 */
                printThreadPoolInfo();

                /**
                 * 如果线程池中的资源已经耗尽，socket赶紧关闭了吧
                 * 服务端已经无能为力了
                 */
                if(processor  == null){
                    System.out.println("从线程池获取可用的HttpProcessor失败！");
                    socket.close();
                }else{
                    /**
                     * 如果线程池中还有可用的资源
                     * 马上把socket分派给这个可用的HttpProcessor，进行后续的处理
                     */
                    processor.assign(socket);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                /**
                 * if catch an Exception
                 * we continue the Http server to listen to the next incoming connection
                 */
                continue;
            }
        }
    }

    /**
     * 启动当前HttpConnector线程
     * 线程启动的逻辑请参考run()
     */
    public void startThread(){
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void printThreadPoolInfo(){
        /**
         *  当前prossors这个stack的大小
         *  也就是当前可用的线程资源
         */
        System.out.println("current Proccessor  stack no: " + processors.size());

        /**
         *  当前有多少HttpProcessor线程对象被创建出来
         *  一般来说，currProccessors肯定是大于等于processors.size()的，
         *  多出来的数量其实就是目前正在处理客户端socket servlet请求的
         */
        System.out.println("current HttpProccessor  no: " + currProccessors);

        /**
         * 打印各个线程使用次数
         */

    }

    /**
     * 这个方法主要功能就是返回HttpProcessor对象
     * 当然绝大部分时间不是简单的new一个HttpProcessor对象
     * 而是要从线程池中去取，这样就大大提升了处理客户端请求的效率，
     * 不用每次都new 一个Socket来处理客户端请求
     * @return 返回HttpProcessor对象
     */
    public HttpProcessor createProcess(){
        HttpProcessor processor = null;

        synchronized (processors){
            /**
             * 如果线程池不为空
             * 直接从线程池中获取一个HttpProcessor对象实例
             */
            if(!processors.empty()){
                return (HttpProcessor) processors.pop();
            }

            if((maxProcessors > 0) && (currProccessors < maxProcessors)) {
                return newProcessor();
            }


//        System.out.println("fetch a thread from the thread pool : " + processor.toString() );
            return processor;

        }

    }

    /**
     * 这个方法用来创建一个新的HttpProcessor实例
     * 实例创建后，马上启动这个线程
     * @return
     */
    public HttpProcessor newProcessor(){
        HttpProcessor processor = new HttpProcessor(this,currProccessors++);

        processor.start();

//        System.out.println("a new thread(HttpProcessor) is created! thread info: " + processor.hashCode() );
        return processor;
    }

    /**
     * 这个方法名字是“回收”
     * 实际上是将HttpProcessor对象放到线程池中去
     * @param processor
     */
    public void recycle(HttpProcessor processor){
        processors.push(processor);
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
    public boolean getEnableLookups() {
        return false;
    }

    @Override
    public void setEnableLookups(boolean enableLookups) {

    }

    @Override
    public ServerSocketFactory getFactory() {
        return null;
    }

    @Override
    public void setFactory(ServerSocketFactory factory) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getRedirectPort() {
        return 0;
    }

    @Override
    public void setRedirectPort(int redirectPort) {

    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public void setScheme(String scheme) {

    }

    @Override
    public boolean getSecure() {
        return false;
    }

    @Override
    public void setSecure(boolean secure) {

    }

    @Override
    public Service getService() {
        return null;
    }

    @Override
    public void setService(Service service) {

    }


    /**
     * 创建Request对象
     * @return
     */
    @Override
    public Request createRequest() {
        HttpRequestImpl request = new HttpRequestImpl();
        request.setConnector(this);
        return request;
    }

    @Override
    public Response createResponse() {
        HttpResponseImpl response = new HttpResponseImpl();
        response.setConnector(this);
        return response;
    }


    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


}
