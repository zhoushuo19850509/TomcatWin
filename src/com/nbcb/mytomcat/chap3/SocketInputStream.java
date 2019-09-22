package com.nbcb.mytomcat.chap3;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class SocketInputStream extends InputStream {

    private InputStream input;
    private int bufferSize;
    private String requestString;   // 整个客户端请求内容

    /**
     * constructor
     * 在constructor中，先读取整个HTTP请求，将整个HTTP请求转化为String
     * @param input
     * @param bufferSize
     */
    public SocketInputStream(InputStream input, int bufferSize){
        this.input = input;
        this.bufferSize = bufferSize;

        /**
         * 先读取整个HTTP请求
         */
        this.requestString = parseWholeRequest();

    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    /**
     * 读取来自客户端的请求，一个完整的请求例子为(通过fileFox访问我们本地启动的服务)：
     */
    /*
     GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
     Host: 127.0.0.1:8080
     User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0
     Accept: text/html,application/xhtml+xml,application/xml;q=0.9
     Accept-Language: en-US,en;q=0.5
     Accept-Encoding: gzip, deflate
     Connection: keep-alive
     Upgrade-Insecure-Requests: 1
    */

    /**
     * 这个方法的总体功能就是，读取整个客户端http请求的第一行：
     * GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
     *
     * 解析出第一行Http请求中的method/Request URI/query string/http headers
     * @param requestLine  将解析结果放到HttpRequestLine对象中
     */
    public void readRequestLine(HttpRequestLine requestLine){
        /**
         * index1 第一行第一个空格的位置
         */
        int index1 = -1;
        index1 = requestString.indexOf(" ");

        /**
         * 根据第一个空格定位到method
         */
        String method = "";
        method = requestString.substring(0,index1);
        System.out.println("method:" + method);

        /**
         * index2 第一行第二个空格的位置
         */
        int index2 = -1;
        if(index1 != -1){
            index2 = requestString.indexOf(" ",index1 + 1);
        }

        /**
         * 根据index2定位到URL请求的位置
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         */
        String completeURI = "";
        if(index2 > index1){
            completeURI = requestString.substring(index1 + 1,index2);
        }
        System.out.println("completeURI:" + completeURI);

        /**
         * index3 第一行第一个回车符(/n)的位置
         */
        int index3 = -1;
        if(index2 != -1){
            index3 = requestString.indexOf("\n",index2 + 1);
        }
        /**
         * 根据index3定位到protocol的位置
         */
        String protocol = "";
        if(index3 > index2 ){
            protocol = requestString.substring(index2 + 1 ,index3);
        }
        System.out.println("protocol:" + protocol);
        System.out.println(index1 + " " + index2 + " " + index3);

        /**
         * 接下来处理URL:
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         * 分别解析出：
         * Request URI: /servlet/PrimitiveServlet
         * Query String: name=zhoushuo&company=nbcb
         */
        int index4 = completeURI.indexOf("?");
        String requestUri = null;
        String queryString = null;
        if(index4 > 0){
            requestUri = completeURI.substring(0,index4);
            queryString = completeURI.substring(index4 + 1,completeURI.length());
            System.out.println("requestUri:" + requestUri);
            System.out.println("queryString:" + queryString);
        }else{
            /**
             * 不带请求参数的情况下
             */
            requestUri = completeURI;
            queryString = "";
        }


        /**
         * 将解析出来的结果放到requestLine对象中
         */
        requestLine.setMethod(method);
        requestLine.setProtocol(protocol);
        requestLine.setUri(requestUri);
        requestLine.setQueryString(queryString);

    }


    /**
     * 从InputStream读取整个客户端请求，包括URL/HTTP Header/cookies等
     * @待优化 这里固定读取2048个字节的内容，显然是不合适的。后需要根据buffer大小循环读
     * @return 返回整个客户端请求
     */
    public String parseWholeRequest(){
        StringBuilder request = new StringBuilder();

        byte[] buffer = new byte[2048];
        int i ;

        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;  // read nothing from client
        }

        for(int j = 0 ; j < i ; j++){
            request.append((char)buffer[j]);
        }

        return request.toString();

    }

    /**
     * 读取来自客户端的HTTP请求的HTTP Header:
     *
     * Host: 127.0.0.1:8080
     * User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0
     * Accept: text/html,application/xhtml+xml,application/xml;q=0.9,
     * Accept-Language: en-US,en;q=0.5
     * Accept-Encoding: gzip, deflate
     * Connection: keep-alive
     * @param header 将解析结果放到HttpHeader对象中去
     */
    public void readHeader(HttpHeader header){

        String[] lines = requestString.split("\n");
        String currentLine = "";
        String key = "";
        String value = "";
        HashMap currentHeaders = new HashMap<String,String>();
        for(int i = 1; i < lines.length ; i++){
            currentLine = lines[i];
            /**
             * 解析每一对http header的key/value
             * 这个if判断主要是应对http header 中一些不是"key:value"形式的非法内容
             */
            if(currentLine.contains(":")){
                key = currentLine.split(":")[0];
                value = currentLine.split(":")[1];
                currentHeaders.put(key, value);
            }

//            System.out.println("key: "+ key + "  value: " + value);
        }
        header.setHeaders(currentHeaders);
    }

    /**
     * main entry for test readHeader
     * @param args
     */
    public static void main(String[] args){

        String requestString = "GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1";

//        int index1 = requestString.indexOf("\n");
//        String headers = requestString.substring(index1 + 1,requestString.length());
//        System.out.println("headers: "+ headers);


        String[] lines = requestString.split("\n");
        String currentLine = "";
        String key = "";
        String value = "";
        for(int i = 1; i < lines.length ; i++){
            currentLine = lines[i];
            key = currentLine.split(":")[0];
            value = currentLine.split(":")[1];
            System.out.println("key: "+ key + "  value: " + value);

        }


    }

    /**
     * main entry for test  readRequestLine
     * @param args
     */
    public static void main1(String[] args){
//        String requestString = "GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1";
        String requestString = "GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1";


        /**
         * index1 第一行第一个空格的位置
         */
        int index1 = -1;
        index1 = requestString.indexOf(" ");

        /**
         * 根据第一个空格定位到method
         */
        String method = "'";
        method = requestString.substring(0,index1);
        System.out.println("method:" + method);

        /**
         * index2 第一行第二个空格的位置
         */
        int index2 = -1;
        if(index1 != -1){
            index2 = requestString.indexOf(" ",index1 + 1);
        }

        /**
         * 根据index2定位到URL请求的位置
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         */
        String completeURI = "";
        if(index2 > index1){
            completeURI = requestString.substring(index1 + 1,index2);
        }
        System.out.println("completeURI:" + completeURI);

        /**
         * index3 第一行第一个回车符(/n)的位置
         */
        int index3 = -1;
        if(index2 != -1){
            index3 = requestString.indexOf("\n",index2 + 1);
        }
        /**
         * 根据index3定位到protocol的位置
         */
        String protocol = "";
        if(index3 > index2 ){
            protocol = requestString.substring(index2 + 1 ,index3);
        }
        System.out.println("protocol:" + protocol);
        System.out.println(index1 + " " + index2 + " " + index3);


        /**
         * 接下来处理URL:
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         * 分别解析出：
         * Request URI: /servlet/PrimitiveServlet
         * Query String: name=zhoushuo&company=nbcb
         */
        int index4 = completeURI.indexOf("?");
        String requestUri = completeURI.substring(0,index4);
        String queryString = completeURI.substring(index4 + 1,completeURI.length());
        System.out.println("requestUri:" + requestUri);
        System.out.println("queryString:" + queryString);

    }
}
