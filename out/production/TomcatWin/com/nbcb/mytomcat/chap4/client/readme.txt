这个package下主要是放一些客户端程序。
主要是通过httpclient，调用tomcat发布的服务。


====================================
httpclient客户端入口程序
StartBatch.java

普通java networking客户端入口程序：
PlainJavaClient
====================================为啥需要通过程序调用呢？
1.因为需要模拟并发；
验证在并发情况下，我们研发的tomcat的线程池是否正常

2.因为通过浏览器调用的话，会有一些不可控的因素
比如，我们发起一个普通的servlet请求：
http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb

通过查看浏览器Network，发现除了这个调用，还有一个静态资源的调用：
http://127.0.0.1:8080/favicon.ico

显然，这是不可控的，这个调用根本不是我们想要的。











