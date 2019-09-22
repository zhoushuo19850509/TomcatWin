import com.nbcb.mytomcat.chap3.HttpProcessor;
import com.nbcb.mytomcat.chap3.HttpRequest;
import com.nbcb.mytomcat.chap3.HttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * �Ȿ����Ҳ��һ��Servlet
 * ��֮ǰPrimitiveServlet��ͬ���ǣ����Servlet�õ���request/response
 * ��HttpServletRequest/HttpServletResponse
 * �ܹ���request/response�õ��������Ϣ(Http header/cookies/parameters)
 */
public class ModernServlet extends HttpServlet {

    /**
     * constructor
     * do nothing
     */
    public ModernServlet() {
    }

    public void init(ServletConfig var1) {
        System.out.println("ModernServlet -- init");
    }

    /**
     * ���doGet���������Servlet��������
     * ���������Ķ�������Ҫ�Ǵ�ӡHttpServletRequest���������
     * HttpServletRequest������֮ǰ���ݿͻ���Http����������������ݣ���ϸ��ο�HttpRequest.java
     * ��Ϊ֮ǰHttpRequest����ʵ����HttpServletRequest�ӿڡ�
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Modern Servlet</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h2>Headers</h2>");


        /**
         * ��ȡrequest�е�http header���ֶΣ���header��key/value��ӡ����
         */
        Enumeration headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            writer.println("<br>" + headerName +  "</br>"); //  " : " + request.getHeader(headerName) + "</br>");
        }

        writer.println("<br><h2>Method</h2>");
        writer.println("<br>" + request.getMethod() + "</br>");
        writer.println("<br><h2>Parameters</h2></br>");


        /**
         * ��ӡ�ͻ���http�����parameter names
         */
        Enumeration parameterNames = request.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            String parameterName = (String)parameterNames.nextElement();
            writer.println("<br>" + parameterName +  "</br>");  // " : " + request.getParameter(parameterName) + "</br>");
        }

        writer.println("<br><h2>Query String</h2></br>");
        writer.println("<br>" + request.getQueryString() + "</br>");
        writer.println("<br><h2>Request URI</h2></br>");
        writer.println("<br>" + request.getRequestURI() + "</br>");
        writer.println("</body>");
        writer.println("</html>");
    }
}


//public class ModernServlet extends HttpServlet{
//
//    public void service(HttpRequest servletRequest, HttpResponse servletResponse) throws ServletException, IOException {
//
//        /**
//         * do something here
//         */
//        System.out.println("servicing by the modern servlet");
//        PrintWriter out = servletResponse.getWriter();
//        out.println("hello hello ,I'm the modern servlet");
//        out.println("very glad to see you");
//
//    }
//
//}
