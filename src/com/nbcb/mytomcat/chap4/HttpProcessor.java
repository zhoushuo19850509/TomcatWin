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
 * @����˵��
 * HttpProcessor���ܺ�Chap3���ƣ�
 * �������Կͻ��˵�socket���󣬽��д���
 * ��chap3��ͬ���ǣ�chap3��process()������ͬ����/����ʽ��
 * һ��ֻ�ܴ���һ���ͻ���socket����
 * chap4����汾��HttpProcessor��HttpConnector�е�һ���߳���Դ
 * ÿ��HttpProcessor����һ���������߳�
 *
 * ���Ƕ�֪����HttpProcessor����Default connector(HttpConnector)�����ġ�
 * ������ʽ���£�
 * 1.HttpConnector���յ�һ�����Կͻ��˵�socket����
 * 2.HttpConnector���̳߳��л�ȡһ��HttpProcessorʵ��
 * 3.HttpConnector����HttpProcessorʵ����assign()������
 *   ����HttpProcessor�ͻ���socket������
 * 4.HttpProcessor֪��֮�󣬾ʹ�������ͻ���socket
 *
 * �ؼ������߳�(HttpConnector)�����߳�(HttpProcessor)���ͨѶ�أ�
 * ����ͨ��assign()/await()
 */
public class HttpProcessor implements Runnable{

    /**
     * ���Կͻ��˵�����
     */
    private Socket socket;

    /**
     * Default connector
     */
    private HttpConnector connector;

    /**
     * ��ǰ���(HttpProcessor)�߳���
     */
    private int currProcessNum;


    /**
     * ���Կͻ���socket����ĵ�һ��
     */
    HttpRequestLine requestLine = new HttpRequestLine();

    private boolean keepAlive = false;

    /**
     * assign()������θ���await()������
     * ��assign()������������(�ͻ���socket�Ѿ�������)��
     * ����ͨ��available�������
     *
     * ��availableΪfalse��˵��assign()�����ã�Ҳ����˵socket��������
     * ��availableΪtrue��˵��assing()���ã�Ҳ����˵socket�Ѿ�׼�����ˣ�await()������ȡ���socket��
     *
     * Ĭ�ϵ�Ȼ�ǲ����õ�(Ĭ��ֵ��construtor�н��г�ʼ��)
     */
    private boolean available;


    /**
     * �������/���ض���
     */
    private HttpRequestImpl request;
    private HttpResponseImpl response;

    /**
     * HttpHeader ����HTTPͷ
     */
    private HttpHeader header;

    /**
     * constructor
     * @param connector Default connector
     * @param currProccessNum the current process number������ʶ��ǰ�̵߳�threadId
     */
    public HttpProcessor(HttpConnector connector, int currProccessNum){
        this.connector = connector;
        this.currProcessNum = currProccessNum;
        available = false;

        /**
         * ��ʼ��request/response����
         */
        this.request = (HttpRequestImpl) connector.createRequest();
        this.response = (HttpResponseImpl) connector.createResponse();

    }


    /**
     * assign���ܼܺ򵥣�������Connector���ã������Կͻ��˵�socket����
     * ��ֵ����ǰHttpProcessor�߳�ʵ�����к����Ĵ���
     * Ϊ�˷�ֹ����assign()/await()����ͬʱ����socket�����¾���������
     * ���������Ҫ����"synchronized"ͬ������
     *
     * @param socket
     */
    synchronized void assign(Socket socket){

        /**
         * �����ǰHttpProcessor���䵽��socket���ڴ���
         * ��ô����ѭ��
         * ֱ��available����Ϊfalse(����ǰHttpProcessor�߳��ͷ�socket�������)
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
     * await()�������ǵȴ�assign()������֪ͨ
     * Ҳ���ǵȴ�Connector�ṩһ���µ����Կͻ��˵�socket����
     *
     * һ��assign()��������await()����assign()������������(Ҳ����available��Ϊtrue)
     * await()���Ͻ�this.socket���ظ�run()���������������socket����
     * @return ����(����HttpConnector��)��ǰ���õĿͻ���socket
     */
    synchronized public Socket await(){

        /**
         * ���availableΪfalse���Ǿ�һֱѭ���ȴ�
         * һֱ�ȵ�assign()�������ǰHttpProcessor�߳�ʵ��������һ��socketʵ��
         * ���˳�ѭ��
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
         * ȡ��socket֮�����ϰ�available��Ϊfalse
         * ˵��assign()���ܱ������ˡ�
         * ��Ȼ���ڵ�ǰHttpProcessor.run()����������socket����֮ǰ��
         * assign()�ǲ����ܱ����õ�
         * ��Ϊ��ǰHttpProcessor�߳��Ѿ����߳��̳߳��ˡ�
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
         * ֻҪDefault connectorû�йرգ�
         * ��ǰHttpProcessor�߳̾�Ҫ��ͣ����
         * �������Կͻ��˵�����
         */
        while (!connector.stopped){

            Socket socket = await();

            /**
             * ���client socketΪ�գ�˵��û�пͻ������󣬼���whileѭ��
             */
            if(socket == null){
                continue;
            }
            process(socket);

            /**
             * ������ͻ�������󣬽���ǰHttpProcess�������
             * ���·Ż�Default connecort��(HttpProcess)�̳߳�
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
     * ����ǰ(���Կͻ��˵�)socket����
     * �����߼������Ϻ�chap3/HttpProcessor.process()��������
     *
     * �޷Ǿ��ǽ����ͻ���socket����
     * ����requestLine�����ֶεĽ���(Method/servletName/Protocol...)
     * ����HttpHeader�Ľ���
     * ����cookies/session�Ľ���
     *
     * �����chap3Ψһ��ͬ�ľ��ǣ�������Կͻ��˵�servlet����
     * ��ר�ŵ�container���д���chap4�д���servlet��container��SimpleContainer
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
             * ����������
             * ��client's socket outputstream���ø�response
             * ��Ϊ����response���õ����outputstream����servlet��ִ�н�����ظ��ͻ���
             */
            output = socket.getOutputStream();
            response.setOutputStream(output);

            /**
             * ����ΪɶҪ��request�������ø�response������
             * ��Ϊ����response������õ�request��ʹ��request�����е�һЩ����(protocol etc)
             */
            response.setRequest(request);

            /**
             * �����ͻ���socket����
             */
            parseRequest(input,output);

            /**
             * ����http header
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
             * �жϿͻ�������URL
             * �������servlet
             * �͵���������Java Container(SimpleContainer)
             * ��ִ��servlet����
             * ���򣬻��ǵ��þ�̬�ļ��ķ���(�ο�chap3)
             */
            if(request.getRequestURI().startsWith("/servlet/")){


                Container container = this.connector.getContainer();
                container.invoke(request,response);

                /**
                 * Ϊ��ģ���̳߳صĻ��ƣ�servlet���ý�����
                 * ������sleepһ��ʱ��
                 */
                System.out.println("HttpProcessor finish invoke the Servlet: " + this.hashCode());

            }else{
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request,response);
            }

            response.finishResponse();

            /**
             * request/response�Ļ���
             * ���շǳ���Ҫ����ΪRequest/Responseʵ������HttpProcessor��contstructor�д����ġ�
             * HttpProcessor�̱߳����̳߳�ȡ����֮��ʹ����Responseʵ��֮���commit�ˡ�
             * �������HttpProcessor�̱߳����̳߳��ٴ�ȡ����֮����Ȼcommit״̬���쳣�ġ�
             * ����Ҫ��Request/Responseʵ���Ĳ����������³�ʼ��
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
         * ��ȡ�ͻ�������(��һ��)���ŵ�HttpRequestLine������
         */
        input.readRequestLine(requestLine);

        /**
         * ����request���������ֶ�
         * ��Щ�ֶ���chap3�ж����Լ����õ�
         *
         * method : GET
         * protocol �� HTTP/1.1
         * queryString �� name=zhoushuo&company=nbcb
         * requestURI  /servlet/PrimitiveServlet ����/index.html
         */
        request.setMethod(requestLine.getMethod());
        request.setProtocol(requestLine.getProtocol());
        request.setQueryString(requestLine.getQueryString());
        request.setRequestURI(requestLine.getUri());

        /**
         * ����queryString�и���parameter������request������
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
     * ���������chap3�н���Http header�ķ�ʽһ��
     * �޷Ǿ��Ǵӿͻ���socket�����н���http header
     * ��ȻҪ����һЩ�����http header ������cookie/session��
     * @param input
     */
    public void parseHeaders(SocketInputStream input){

        header = new HttpHeader();

        /**
         * ��ͨ��SocketInputStream�ӿͻ��������н�����http header
         */
        input.readHeader(header);


        /**
         * ѭ������HttpHeader
         * ���϶�ȡ�ͻ��������е�Http Header�ֶ�
         * ���������Http Header�ֶΣ��������⴦������cookies/session��
         */
        HashMap<String,String> headers = header.getHeaders();
        String name = "";   // key of http header
        String value = "";  // value of http header
        for(Map.Entry<String, String> entry : headers.entrySet()){
            name = entry.getKey();
            value = entry.getValue();
            request.addHeader(name ,value);

            /**
             * ����һЩ�����Http header
             */
            if(name.equals("cookie")){   // �������Կͻ��˵�cookie��Ϣ

                Cookie[] cookies = RequestUtil.parseCookieHeaders(value);

                /**
                 * ����cookies ��jsession��ȡ���������õ�request��ȥ
                 * ͬʱ�Ѹ���Cookies����Ҳadd ��request��ȥ
                 */
                for(int i = 0 ; i < cookies.length; i++){
                    if(cookies[i].getName().equals("jsessionid")){
                        request.setRequestedSessionCookie(true);
                        request.setRequestedSessionId(cookies[i].getValue());
                    }
                    request.addCookie(cookies[i]);
                }

            }else if(name.equals("content-length")){  // content����

                int n = -1;
                n = Integer.parseInt(value);
                request.setContentLength(n);
            }else if(name.equals("content-type")){  // content����

                request.setContentType(value);
            }

        }

    }
}
