

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * 这本质上也是一个Servlet
 * 和之前PrimitiveServlet不同的是，这个Servlet拿到的request/response
 * 是HttpServletRequest/HttpServletResponse
 * 能够从request/response拿到更多的信息(Http header/cookies/parameters)
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
     * 这个doGet方法是这个Servlet的主方法
     * 这个方法里的东西，主要是打印HttpServletRequest对象的内容
     * HttpServletRequest是我们之前根据客户端Http请求解析出来的内容，详细请参考HttpRequest.java
     * 因为之前HttpRequest就是实现了HttpServletRequest接口。
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
        writer.println("<h2>Headers of WEB-INF new2</h2>");


        /**
         * 获取request中的http header的字段，将header的key/value打印出来
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
         * 打印客户端http请求的parameter names
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

    public void sayHello(){
        System.out.println("hello from ModernServlet file");
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
