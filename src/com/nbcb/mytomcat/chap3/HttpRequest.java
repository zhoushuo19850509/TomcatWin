package com.nbcb.mytomcat.chap3;

import com.nbcb.mytomcat.util.ParameterMap;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class HttpRequest implements HttpServletRequest{

    private String method = "";      // GET
    private String protocol = "";    // HTTP/1.1
    private String queryString = ""; // name=zhoushuo&company=nbcb
    private String requestURI = "";         // /servlet/PrimitiveServlet 或者/index.html

    /**
     * http header
     */
    protected HashMap headers = new HashMap();

    /**
     * cookies from the client
     */
    protected ArrayList cookies = new ArrayList();


    /**
     * http header的Cookies中是否包含session id
     */
    protected boolean requestedSessionCookie = false;

    /**
     * http header的Cookies中包含的session id
     */
    protected String requestedSessionId = "";




    /**
     * parameter key/values of the client's request url
     */
//    protected ParameterMap parameters = new ParameterMap();
    protected HashMap<String,String> parameters = new HashMap<String,String>();


    /**
     * contentLength
     * Http请求体 content的长度
     * @param requestURI
     */
    private int contentLength = 0;


    protected String contentType = "";

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    /**
     * 将我们从客户端请求中解析出来的http header放到headers中去
     * @param name
     * @param value
     */
    public void addHeader(String name ,String value){
        headers.put(name ,value);
    }

    public void addParameter(String key,String value){
        parameters.put(key,value);
    }

    /**
     * 从Http header中解析出cookie信息
     * @param cookie
     */
    public void addCookie(Cookie cookie){
        this.cookies.add(cookie);
    }


    private InputStream input;

    public HttpRequest(InputStream input){
        this.input = input;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return (String)headers.get(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }




    @Override
    public Enumeration<String> getHeaderNames() {

        Enumeration<String> headerNames;
        Vector<String> headerNamesVector = new Vector<String>();
        for(Object headerName: headers.keySet()){

            headerNamesVector.add((String)headerName);
        }
        headerNames = headerNamesVector.elements();
        return headerNames;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }


    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }


    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }



    @Override
    public int getContentLength() {
        return this.contentLength;
    }


    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return parameters.get(s);
    }

    @Override
    public Enumeration<String> getParameterNames() {

        Enumeration<String> parameterNames;
        Vector<String> parameterNameVector = new Vector<String>();
        for(Object parameterName: parameters.keySet()){
            parameterNameVector.add((String)parameterName);
        }
        parameterNames = parameterNameVector.elements();
        return parameterNames;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    public boolean isRequestedSessionCookie() {
        return requestedSessionCookie;
    }

    public void setRequestedSessionCookie(boolean requestedSessionCookie) {
        this.requestedSessionCookie = requestedSessionCookie;

    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }


}
