package com.nbcb.mytomcat.chap4;

import com.nbcb.mytomcat.util.RequestUtil;
import org.apache.catalina.Container;
import org.apache.catalina.Request;
import org.apache.catalina.util.FastHttpDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @功能说明
 * HttpProcessor功能和Chap3类似：
 * 接收来自客户端的socket请求，进行处理
 * 和chap3不同的是，chap3的process()方法是同步的/阻塞式的
 * 一次只能处理一个客户端socket请求
 * chap4这个版本的HttpProcessor是HttpConnector中的一个线程资源
 * 每个HttpProcessor都是一个独立的线程
 *
 * 我们都知道，HttpProcessor是由Default connector(HttpConnector)触发的。
 * 触发方式如下：
 * 1.HttpConnector接收到一个来自客户端的socket请求
 * 2.HttpConnector从线程池中获取一个HttpProcessor实例
 * 3.HttpConnector调用HttpProcessor实例的assign()方法，
 *   告诉HttpProcessor客户端socket可用了
 * 4.HttpProcessor知道之后，就处理这个客户端socket
 *
 * 关键是主线程(HttpConnector)和子线程(HttpProcessor)如何通讯呢？
 * 就是通过assign()/await()
 */
public class HttpProcessor implements Runnable{

    /**
     * 来自客户端的请求
     */
    private Socket socket;

    /**
     * Default connector
     */
    private HttpConnector connector;

    /**
     * 当前活动的(HttpProcessor)线程数
     */
    private int currProcessNum;


    /**
     * 来自客户端socket请求的第一行
     */
    HttpRequestLine requestLine = new HttpRequestLine();

    private boolean keepAlive = false;

    /**
     * assign()方法如何告诉await()方法，
     * 我assign()方法被调用了(客户端socket已经可用了)呢
     * 就是通过available这个变量
     *
     * 当available为false，说明assign()不可用，也就是说socket还不可用
     * 当available为true，说明assing()可用，也就是说socket已经准备好了，await()可以来取这个socket了
     *
     * 默认当然是不可用的(默认值在construtor中进行初始化)
     */
    private boolean available;


    /**
     * 请求对象/返回对象
     */
    private HttpRequestImpl request;
    private HttpResponseImpl response;

    /**
     * HttpHeader 保存HTTP头
     */
    private HttpHeader header;

    /**
     * constructor
     * @param connector Default connector
     * @param currProccessNum the current process number用来标识当前线程的threadId
     */
    public HttpProcessor(HttpConnector connector, int currProccessNum){
        this.connector = connector;
        this.currProcessNum = currProccessNum;
        available = false;

        /**
         * 初始化request/response对象
         */
        this.request = (HttpRequestImpl) connector.createRequest();
        this.response = (HttpResponseImpl) connector.createResponse();

    }


    /**
     * assign功能很简单，就是由Connector调用，将来自客户端的socket请求
     * 赋值给当前HttpProcessor线程实例进行后续的处理。
     * 为了防止出现assign()/await()方法同时处理socket，导致竞争的问题
     * 这个方法需要加上"synchronized"同步机制
     *
     * @param socket
     */
    synchronized void assign(Socket socket){

        /**
         * 如果当前HttpProcessor分配到的socket正在处理
         * 那么无限循环
         * 直到available被置为false(即当前HttpProcessor线程释放socket处理对象)
         */
        while(available){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.socket = socket;
        available = true;
        notifyAll();
    }

    /**
     * await()方法就是等待assign()方法的通知
     * 也就是等待Connector提供一个新的来自客户端的socket连接
     *
     * 一旦assign()方法告诉await()，我assign()方法被调用了(也就是available置为true)
     * await()马上将this.socket返回给run()方法，来处理这个socket对象
     * @return 返回(来自HttpConnector的)当前可用的客户端socket
     */
    synchronized public Socket await(){

        /**
         * 如果available为false，那就一直循环等待
         * 一直等到assign()对象给当前HttpProcessor线程实例分配了一个socket实例
         * 才退出循环
         */
        while(!available){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Socket socket = this.socket;

        /**
         * 取到socket之后，马上把available置为false
         * 说明assign()又能被调用了。
         * 当然，在当前HttpProcessor.run()方法处理完socket请求之前，
         * assign()是不可能被调用的
         * 因为当前HttpProcessor线程已经被踢出线程池了。
         *
         */
        available = false;
        notifyAll();

        return socket;
    }

    @Override
    public void run() {

        System.out.println("HttpProcessor start listening from client' connection " + this.hashCode());
        /**
         * 只要Default connector没有关闭，
         * 当前HttpProcessor线程就要不停工作
         * 接收来自客户端的请求
         */
        while (!connector.stopped){

            Socket socket = await();

            /**
             * 如果client socket为空，说明没有客户端请求，继续while循环
             */
            if(socket == null){
                continue;
            }
            process(socket);

            /**
             * 处理完客户端请求后，将当前HttpProcess对象回收
             * 重新放回Default connecort的(HttpProcess)线程池
             */
//            System.out.println("HttpProcessor finish processing" +
//                    " the client's request, start recycle !");
            connector.recycle(this);

        }

    }

    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * 处理当前(来自客户端的)socket连接
     * 处理逻辑大体上和chap3/HttpProcessor.process()方法类似
     *
     * 无非就是解析客户端socket请求
     * 包括requestLine各个字段的解析(Method/servletName/Protocol...)
     * 包括HttpHeader的解析
     * 包括cookies/session的解析
     *
     * 这里和chap3唯一不同的就是，针对来自客户端的servlet请求
     * 用专门的container进行处理，chap4中处理servlet的container是SimpleContainer
     * @param socket
     */
    public void process(Socket socket){
        System.out.println("HttpProcessor start process the client's socket: " + this.hashCode());
        SocketInputStream input = null;
        OutputStream output = null;
        boolean ok = true;

        try {
             input = new SocketInputStream(
                    socket.getInputStream(),
                    connector.getBufferSize());

            /**
             * 这两句好理解
             * 将client's socket outputstream设置给response
             * 因为后续response会用到这个outputstream，将servlet的执行结果返回给客户端
             */
            output = socket.getOutputStream();
            response.setOutputStream(output);

            /**
             * 这里为啥要把request对象设置给response对象呢
             * 因为后续response对象会用到request，使用request对象中的一些内容(protocol etc)
             */
            response.setRequest(request);

            /**
             * 解析客户端socket请求
             */
            parseRequest(input,output);

            /**
             * 解析http header
             */
            parseHeaders(input);

//            ackRequest(output);

            /**
             * set response http header
             *
             * Http header of Nginx
             * Connection:keep-alive
             Data:Sat, 14 Sep 2019 09:13:49 GMT
             Etag:"59746037-264"
             Last-modified:Sun, 23 Jul 2017 08:37:11 GMT
             Server:nginx/1.13.3
             */
            ((HttpServletResponse)response).setHeader("Connection","keep-alive");
            ((HttpServletResponse)response).setHeader("Server","MyTomcat-chap4");
            ((HttpServletResponse)response).setHeader("Date", FastHttpDateFormat.getCurrentDate());
            ((HttpServletResponse)response).setHeader("Etag","59746037-266");


            /**
             * 判断客户端请求URL
             * 如果包含servlet
             * 就调用我们新Java Container(SimpleContainer)
             * 来执行servlet请求
             * 否则，还是调用静态文件的方法(参考chap3)
             */
            if(request.getRequestURI().startsWith("/servlet/")){


                Container container = this.connector.getContainer();
                container.invoke(request,response);

                /**
                 * 为了模拟线程池的机制，servlet调用结束后
                 * 在这里sleep一段时间
                 */
                System.out.println("HttpProcessor finish invoke the Servlet: " + this.hashCode());

            }else{
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request,response);
            }

            response.finishResponse();

            /**
             * request/response的回收
             * 回收非常重要，因为Request/Response实例是在HttpProcessor的contstructor中创建的。
             * HttpProcessor线程被从线程池取出来之后，使用完Response实例之后就commit了。
             * 后续这个HttpProcessor线程被从线程池再次取出来之后，显然commit状态是异常的。
             * 所以要对Request/Response实例的参数进行重新初始化
             */
            request.recycle();
            response.recycle();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } finally{
            /**
             * close the resource finally
             */
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private static final byte[] ack =
            (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();


    /**
     * return "HTTP/1.1 100 Continue" to the client
     * @param out
     */
    private void ackRequest(OutputStream out){
        try {
            out.write(ack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void parseRequest(SocketInputStream input, OutputStream output){


        /**
         * 读取客户端请求(第一行)，放到HttpRequestLine对象中
         */
        input.readRequestLine(requestLine);

        /**
         * 设置request对象的相关字段
         * 这些字段在chap3中都是自己设置的
         *
         * method : GET
         * protocol ： HTTP/1.1
         * queryString ： name=zhoushuo&company=nbcb
         * requestURI  /servlet/PrimitiveServlet 或者/index.html
         */
        request.setMethod(requestLine.getMethod());
        request.setProtocol(requestLine.getProtocol());
        request.setQueryString(requestLine.getQueryString());
        request.setRequestURI(requestLine.getUri());

        /**
         * 遍历queryString中各个parameter，塞到request对象中
         */
        String queryString = requestLine.getQueryString();
        String parameter = "";
        String key = "";
        String value = "";
        HashMap<String,String> parameterMap = new HashMap<String,String>();
        if(queryString != null && queryString != "" && queryString.length() > 0){
            String[] parameters = queryString.split("&");
            if(parameters != null && parameters.length > 0){
                for(int i = 0 ; i < parameters.length; i++){
                    parameter = parameters[i];
                    if(parameter.contains("=")){
                        key = parameter.split("=")[0];
                        value = parameter.split("=")[1];
                        request.addParameter(key,value);
                    }
                }
            }
        }
    }

    /**
     * 这个方法和chap3中解析Http header的方式一样
     * 无非就是从客户端socket请求中解析http header
     * 当然要处理一些特殊的http header ，比如cookie/session等
     * @param input
     */
    public void parseHeaders(SocketInputStream input){

        header = new HttpHeader();

        /**
         * 先通过SocketInputStream从客户端请求中解析出http header
         */
        input.readHeader(header);


        /**
         * 循环遍历HttpHeader
         * 不断读取客户端请求中的Http Header字段
         * 对于特殊的Http Header字段，进行特殊处理，比如cookies/session等
         */
        HashMap<String,String> headers = header.getHeaders();
        String name = "";   // key of http header
        String value = "";  // value of http header
        for(Map.Entry<String, String> entry : headers.entrySet()){
            name = entry.getKey();
            value = entry.getValue();
            request.addHeader(name ,value);

            /**
             * 处理一些特殊的Http header
             */
            if(name.equals("cookie")){   // 处理来自客户端的cookie信息

                Cookie[] cookies = RequestUtil.parseCookieHeaders(value);

                /**
                 * 遍历cookies 把jsession抽取出来，设置到request中去
                 * 同时把各个Cookies对象也add 到request中去
                 */
                for(int i = 0 ; i < cookies.length; i++){
                    if(cookies[i].getName().equals("jsessionid")){
                        request.setRequestedSessionCookie(true);
                        request.setRequestedSessionId(cookies[i].getValue());
                    }
                    request.addCookie(cookies[i]);
                }

            }else if(name.equals("content-length")){  // content长度

                int n = -1;
                n = Integer.parseInt(value);
                request.setContentLength(n);
            }else if(name.equals("content-type")){  // content类型

                request.setContentType(value);
            }

        }

    }
}
