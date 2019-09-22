代码解读


1.入口 BootStrap.java

2.HttpConnector.java
监听来自客户端的socket请求
新起线程,创建ServerSocket对象，监听来自客户端的socket请求：


3.HttpProcessor处理来自客户端的socket请求
HttpConnector一旦接收到来自客户端的socket请求，就把这个socket交给
HttpProcessor.process()方法进行处理

HttpProcessor中的业务逻辑很多：
parseRequest()：解析客户端请求(第一行) : GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
parseHeaders()：解析客户端请求的http header
最后根据客户端请求URL判断是servlet请求还是static页面请求，调用相应的处理类




4.
SocketInputStream.java
因为SocketInputStream处理客户端socket的输入流(inputstream)，因此这个类的逻辑最丰富，
包括：
parseWholeRequest() // 读取客户端请求的整体报文
parseRequest()   // 读取客户端Http请求第一行
readHeader()     // 读取客户端Http请求的Http headers


5.
HttpRequest.java
HttpResponse.java

总体来说，这两个类是成对出现的。
其中HttpRequest包含了很多内容，主要是我们在HttpProcessor中，根据客户端socket获取的各种信息
包括servletName/staticURL,http headers,cookies等等
后续如果是执行本地servlet实例，会把HttpRequest传进去，供servlet消费这些解析出来的内容。

这一对request/response和第一章/第二章的request/response有所不同
之前的是实现ServletRequest/ServletResponse接口
这里是实现HttpRequest/HttpResponse接口

具体有什么不同呢？
我们看一下HttpServletRequest类的定义就知道了：
HttpServletRequest extends ServletRequest
HttpServletRequest是继承自ServletRequest，很显然，
HttpServletRequest的功能比ServletRequest更加丰富

具体体现在我们这章实现的：
在应用层(HTTP)，新增了很多内容，包括
parameters(保存在ParameterMap)
http headers()
cookies
等等

后续我们的Servlet，只要继承HttpServlet类，就能享受到
HttpServletRequest/HttpServletResponse对象中更加丰富的内容

6.HttpRequestLine
这是一个JavaBean，保存的是客户端HTTP请求第一行

7.处理类
ServletProcessor.java  // 处理动态资源，加载本地servlet类，加载方式和chap2一样
StaticResourceProcessor.java // 处理静态资源，加载本地静态资源，加载方式和chap1一样