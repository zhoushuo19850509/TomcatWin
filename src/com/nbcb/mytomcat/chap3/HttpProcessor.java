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

    private HttpRequestLine requestLine = null;  // ����ͻ��������һ�е����ݣ�����method/protocol/query string/uri��
    private HttpHeader header;   // ����HTTP Header��Ϣ

    private HttpRequest request = null;
    private HttpResponse response = null;

    /**
     * constructor
     * ��Ҫ��ʵ����һЩ����
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
     * ����ͻ�������URLΪ��
     * GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
     * �����ڵڶ���ʵ����servlet��Ľ�����PrimitiveServlet
     * ������Ҫʵ�ֶ�parameter�Ľ����� name=zhoushuo&company=nbcb
     */
    public void parseRequest(SocketInputStream input, OutputStream output){

        /**
         * ��ȡ�ͻ�������(��һ��)���ŵ�HttpRequestLine������
         */
        input.readRequestLine(requestLine);

        /**
         * Ȼ���HttpRequestLine�����еĸ����ֶ����õ�request������ȥ
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
