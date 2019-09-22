package com.nbcb.mytomcat.chap4;

import com.nbcb.mytomcat.chap3.HttpRequest;
import com.nbcb.mytomcat.chap3.HttpResponse;
import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.Request;
import org.apache.catalina.Response;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * ���Processor��Ҫ�������Կͻ��˵ľ�̬��Դ����
 */
public class StaticResourceProcessor {

    private static final int BUFFER_SIZE = 1024;

    /**
     * ����HttpRequest/HttpResponse ��
     * �������Կͻ��˵ľ�̬��Դ����
     * @param request
     * @param response
     */
    public void process(Request request, Response response){

        /**
         * the File object(static resource) ,that the client request
         */
        File file = new File(Constants.WEB_ROOT , ((HttpServletRequest)request).getRequestURI());

        FileInputStream fis = null;

        OutputStream output = null;

        byte[] bytes = new byte[BUFFER_SIZE];

        try {
            output = ((HttpServletResponse)response).getOutputStream();
            if(file.exists()){
                fis = new FileInputStream(file);
                int ch = fis.read(bytes,0,BUFFER_SIZE);
                /**
                 * ���whileѭ������˼�ǣ�ÿ�ζ���FileInputStream��
                 * ��ȡ�̶����ȵ�����(����ΪBUFFER_SIZE)
                 */
                while(ch != -1){
                    output.write(bytes,0,BUFFER_SIZE);
                    ch = fis.read(bytes,0,BUFFER_SIZE);
                }
            }else{
                String errorMsg = "HTTP/1.1 404 File Not Found" +
                        "<h1>error</h1>";
                output.write(errorMsg.getBytes());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * send the file content back to the client as OutputStream
         */

    }
}
