本章重要代码罗列如下：

1.BootStrap.java
2.HttpConnector.java
3.HttpProcessor.java
4.SimpleContainer.java

其中HttpProcessor.java显然是最难啃的

==========================================
详细说明一下各个代码：



==========================================
1.BootStrap.java
启动类，作为代码入口

==========================================
2.HttpConnector.java
自己实现的Connector类，这个类实现了catalina.Connector接口

HttpConnector.java中最重要的方法有：
getContainer()/setContainer()   // 设置container，用于后处理后续的servlet请求
initialize()   // 启动ServerSocket，
start()        // 启动HttpConnector主线程，同时初始化(Httpprocessor)线程池
run()          // HttpConnector的主线程方法，while循环，开始监听来自客户端的socket请求

这章改进的地方有：
支持多线程:通过维护一个(HttpProcessor)线程池，实现了处理并发客户端请求的功能


这个显然要比我们之前在第三章的HttpConnector要专业。
具体请参考Connector接口中的注释

关于Connector的功能，我们已经在第三章已经简单领略过了：就是启动一个Socket监听，
处理来自客户端的Socket请求，然后把服务端的处理结果返回给客户端

==========================================
3.HttpProcessor.java
Http处理类

处理来自客户端的socket请求

我们知道，之前HttpConnector通过维护一个(HttpProcessor)线程池，
实现了并发支持客户端socket连接的功能。
那么，HttpProcessor类，必然是一个线程类。
我们知道，所谓的线程池，就是提前准备一些线程类的实例，从要用的时候，马上从线程池中取出一个实例。
HttpProcessor也是一样，一旦HttpProcessor实例放到线程池中，都会马上启动HttpProcessor Thread instance
HttpProcessor.run()方法中，会启动一个while()循环，一旦有客户端socket请求过来，就开始处理。
因为HttpProcessor是线程池中的实例，必然会面临同步的问题。因此需要加入synchronized处理。

实现了HttpProcessor多线程之后，后续的那些逻辑和chap3/HttpProcessor其实是类似的，无非就是:
解析客户端请求(第一行);
解析客户端请求的http header

chap4更加进一步：除了解析客户端请求，还要根据客户端请求的protocol，支持http1.1协议
怎么支持HTTP1.1协议呢？
比如客户端请求为：
GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1

"HTTP/1.1"说明客户端是通过HTTP/1.1协议访问服务端的。
这是服务端就是实现：
(1)Http长连接
(2)chunk

==========================================
4.SimpleContainer.java
这章引入了Container的概念
SimpleContainer实现了tomcat的Container接口：
org.apache.catalina.Container.java

SimpleContainer起的作用和chap3/ServletProcessor类似。
只不过ServletProcessor更加专业，实现了Container接口

SimpleContainer.java最关键的方法是；
invoke(Request request, Response response)
主要跟load 一个 servlet类，同时运行这个servlet的service()方法，执行servlet类


==========================================
5.
HttpRequestImpl.java/HttpResponseImpl.java

这两个Request/Response对象，继承关系请参考《How Tomcat works》这本书

HttpRequestImpl中要做什么事情呢？这个我们在chap3中已经实践过了，无非就是能够获取
http headers/http parameters等等

HttpResponseImpl无非就是getWriter()，打印到控制台嘛


==========================================chap3
6.SocketInputStream
功能和chap3类似：解析客户端socket请求内容

7.HttpRequestLine
功能和chap3类似：这个对象保存的是客户端socket请求内容的第一行



