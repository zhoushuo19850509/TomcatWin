package com.nbcb.mytomcat.chap3;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class SocketInputStream extends InputStream {

    private InputStream input;
    private int bufferSize;
    private String requestString;   // �����ͻ�����������

    /**
     * constructor
     * ��constructor�У��ȶ�ȡ����HTTP���󣬽�����HTTP����ת��ΪString
     * @param input
     * @param bufferSize
     */
    public SocketInputStream(InputStream input, int bufferSize){
        this.input = input;
        this.bufferSize = bufferSize;

        /**
         * �ȶ�ȡ����HTTP����
         */
        this.requestString = parseWholeRequest();

    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    /**
     * ��ȡ���Կͻ��˵�����һ����������������Ϊ(ͨ��fileFox�������Ǳ��������ķ���)��
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
     * ������������幦�ܾ��ǣ���ȡ�����ͻ���http����ĵ�һ�У�
     * GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
     *
     * ��������һ��Http�����е�method/Request URI/query string/http headers
     * @param requestLine  ����������ŵ�HttpRequestLine������
     */
    public void readRequestLine(HttpRequestLine requestLine){
        /**
         * index1 ��һ�е�һ���ո��λ��
         */
        int index1 = -1;
        index1 = requestString.indexOf(" ");

        /**
         * ���ݵ�һ���ո�λ��method
         */
        String method = "";
        method = requestString.substring(0,index1);
        System.out.println("method:" + method);

        /**
         * index2 ��һ�еڶ����ո��λ��
         */
        int index2 = -1;
        if(index1 != -1){
            index2 = requestString.indexOf(" ",index1 + 1);
        }

        /**
         * ����index2��λ��URL�����λ��
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         */
        String completeURI = "";
        if(index2 > index1){
            completeURI = requestString.substring(index1 + 1,index2);
        }
        System.out.println("completeURI:" + completeURI);

        /**
         * index3 ��һ�е�һ���س���(/n)��λ��
         */
        int index3 = -1;
        if(index2 != -1){
            index3 = requestString.indexOf("\n",index2 + 1);
        }
        /**
         * ����index3��λ��protocol��λ��
         */
        String protocol = "";
        if(index3 > index2 ){
            protocol = requestString.substring(index2 + 1 ,index3);
        }
        System.out.println("protocol:" + protocol);
        System.out.println(index1 + " " + index2 + " " + index3);

        /**
         * ����������URL:
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         * �ֱ��������
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
             * ������������������
             */
            requestUri = completeURI;
            queryString = "";
        }


        /**
         * �����������Ľ���ŵ�requestLine������
         */
        requestLine.setMethod(method);
        requestLine.setProtocol(protocol);
        requestLine.setUri(requestUri);
        requestLine.setQueryString(queryString);

    }


    /**
     * ��InputStream��ȡ�����ͻ������󣬰���URL/HTTP Header/cookies��
     * @���Ż� ����̶���ȡ2048���ֽڵ����ݣ���Ȼ�ǲ����ʵġ�����Ҫ����buffer��Сѭ����
     * @return ���������ͻ�������
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
     * ��ȡ���Կͻ��˵�HTTP�����HTTP Header:
     *
     * Host: 127.0.0.1:8080
     * User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0
     * Accept: text/html,application/xhtml+xml,application/xml;q=0.9,
     * Accept-Language: en-US,en;q=0.5
     * Accept-Encoding: gzip, deflate
     * Connection: keep-alive
     * @param header ����������ŵ�HttpHeader������ȥ
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
             * ����ÿһ��http header��key/value
             * ���if�ж���Ҫ��Ӧ��http header ��һЩ����"key:value"��ʽ�ķǷ�����
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
         * index1 ��һ�е�һ���ո��λ��
         */
        int index1 = -1;
        index1 = requestString.indexOf(" ");

        /**
         * ���ݵ�һ���ո�λ��method
         */
        String method = "'";
        method = requestString.substring(0,index1);
        System.out.println("method:" + method);

        /**
         * index2 ��һ�еڶ����ո��λ��
         */
        int index2 = -1;
        if(index1 != -1){
            index2 = requestString.indexOf(" ",index1 + 1);
        }

        /**
         * ����index2��λ��URL�����λ��
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         */
        String completeURI = "";
        if(index2 > index1){
            completeURI = requestString.substring(index1 + 1,index2);
        }
        System.out.println("completeURI:" + completeURI);

        /**
         * index3 ��һ�е�һ���س���(/n)��λ��
         */
        int index3 = -1;
        if(index2 != -1){
            index3 = requestString.indexOf("\n",index2 + 1);
        }
        /**
         * ����index3��λ��protocol��λ��
         */
        String protocol = "";
        if(index3 > index2 ){
            protocol = requestString.substring(index2 + 1 ,index3);
        }
        System.out.println("protocol:" + protocol);
        System.out.println(index1 + " " + index2 + " " + index3);


        /**
         * ����������URL:
         * /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb
         * �ֱ��������
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
