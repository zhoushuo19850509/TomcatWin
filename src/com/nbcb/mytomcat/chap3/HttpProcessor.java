package com.nbcb.mytomcat.chap3;

import com.nbcb.mytomcat.util.RequestUtil;
import jdk.internal.util.xml.impl.Input;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpProcessor {

    private HttpRequestLine requestLine = null;  // 保存客户端请求第一行的内容，包括method/protocol/query string/uri等
    private HttpHeader header;   // 保存HTTP Header信息

    private HttpRequest request = null;
    private HttpResponse response = null;

    /**
     * constructor
     * 主要是实例化一些对象
     */
    public HttpProcessor(){
        requestLine = new HttpRequestLine();

    }


    public void proccess(Socket socket){


        SocketInputStream input = null;
        OutputStream output = null;



        try {
            input = new SocketInputStream(socket.getInputStream(),2048);
            output = socket.getOutputStream();

            request = new HttpRequest(input);
            response = new HttpResponse(output);

            parseRequest(input,output);
            parseHeaders(input);


            if(request.getRequestURI().startsWith("/servlet/")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(request,response);
            }else{
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request,response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * parse the request url from client
     * 比如客户端请求URL为：
     * GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
     * 我们在第二章实现了servlet类的解析：PrimitiveServlet
     * 第三章要实现对parameter的解析： name=zhoushuo&company=nbcb
     */
    public void parseRequest(SocketInputStream input, OutputStream output){

        /**
         * 读取客户端请求(第一行)，放到HttpRequestLine对象中
         */
        input.readRequestLine(requestLine);

        /**
         * 然后把HttpRequestLine对象中的各个字段设置到request对象中去
         */
        request.setQueryString(requestLine.getQueryString());
        request.setRequestURI(requestLine.getUri());
        request.setMethod(requestLine.getMethod());
        request.setProtocol(requestLine.getProtocol());

        /**
         * parse parameters from querystring :
         * name=zhoushuo&company=nbcb
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
     * parse the http header of client's request
     * and add the key/value in http header to the request(HttpRequest)
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
