package com.nbcb.mytomcat.chap3;


/**
 * 来自客户端的请求URL：
 * GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
 */
public class HttpRequestLine {

    private String method;      // GET
    private String protocol;    // HTTP/1.1
    private String queryString; // name=zhoushuo&company=nbcb
    private String uri;         // /servlet/PrimitiveServlet


    /**
     * getter() and setter()
     * @return
     */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
