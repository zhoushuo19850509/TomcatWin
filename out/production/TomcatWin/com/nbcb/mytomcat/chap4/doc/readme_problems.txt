
这个文档主要记载实践这章的时候碰到的一些问题

这章碰到的主要问题是：
1.一个类实现了多个接口，这些接口中的方法不统一
2.

详细请参考如下：

===============================问题1 HttpResponseImpl创建的时候报错
问题描述：
我们创建HttpResponseImpl的时候，报了一个错：

这个方法不对
public List<String> getHeaders(String s) {
        return null;
}

问题分析：
为啥不对呢？
因为我们看类结构图，
HttpResponseImpl继承了org.apache.catalina.connector.HttpResponseBase
而HttpResponseBase既实现了org.apache.catalina.HttpResponse接口，
又实现了javax.servlet.http.HttpServletResponse接口。
比较搞笑的是，可能是因为版本的关系，

HttpResponse 接口有如下方法：
public String[] getHeaderNames();

HttpServletResponse 接口有如下方法：
Collection<String> getHeaderNames();

我们看到这两个方法，方法名一样，但是返回值不一样
这样就会报冲突
这个以后写代码的时候，要特别注意。
虽然一个Implement类可以集成多个接口，但是要特别注意这些接口不会有相同名称的方法
一旦不同的接口有相同名称的方法，返回值必须一样，当然参数可以不一样。

问题解决方案：
解决方式也很简单，就是将两个接口同名方法的返回类型改为一致：
HttpResponse接口方法：
public String[] getHeaderNames();
-------->改为：
public List<String> getHeaderNames();

以后类似的问题也可以用同样的方法：
如果老版的tomcat Java代码不适用，就修改成新版本的java代码

===============================问题2 HttpRequestBase.java报错
报错内容：大致意思就是HttpRequest.java实现了ServletRequest接口，
但是没有实现ServletRequest.isAsyncSupported()的接口方法：

public class HttpRequestBase
    extends RequestBase
    implements HttpRequest, HttpServletRequest {...}


问题分析：
为啥会出现这个问题呢？
我们看到HttpRequestBase既实现了HttpRequest接口(org.apache.catalina.HttpRequest)
又实现了HttpServletRequest接口(javax.servlet.http.HttpServletRequest)

为啥会报HttpRequestBase没有实现HttpServletRequest接口的部分方法呢？
因为我们集成到工程的servlet jar包是比较新的。
随着servlet标准的演进，servlet接口肯定是不断在扩充的。
因此servlet接口不断有新的接口方法需要实现。

问题解决方式1：
简单的解决方式，当然是HttpRequestBase类中扩充一下HttpServletRequest的实现方法(当然可以是空方法，不具体实现)

问题解决方式2：
换一个比较老的servlet jar包

经验总结：
我们看到比较老的tomcat，比如tomcat4，和最新的servlet标准规范是不兼容的。
后续我们在实际研发中要特别注意。有时间的话，最好研读一下servlet的接口规范。




===============================问题3 工程编译报错
我们知道，自从chap4开始，我们就要实现tomcat catalina的一些接口
但是我们引入org.apache.catalina之后，有一些编译报错
原因其实很简单，其实就是我们的JDK环境/catalina程序包/servlet版本是不一致的：

1.我们org.apache.catalina的包是tomcat4版本的:
/home/zhoushuo/Documents/testGit/HowTomcatWorks/how-tomcat-works-util/src/main/java/org/apache/catalina
这个版本是HowTomcatWorks的中文译者的代码实践github
2.我们引用的servlet包是从tomcat8 lib目录拷过来的
(/home/zhoushuo/Downloads/apache-tomcat-8.5.23/lib/servlet-api.jar)
3.我们的JDK是IDEA中的JDK8

解决方案：
当前工程下列三个要素要配套：
1.JDK版本

2.tomcat相关接口
org.apache.catalina

3.servlet相关
javax.servlet

所以要搞清楚，每个Tomcat版本对应的JDK和servlet的版本分别是啥，
具体参考doc/readme_tomcat_version.txt

那么我们直接用Tomcat8那一套可以吗？
显然不行，为啥呢？因为Tomcat8那一套代码和书本中的Tomcat4代码相比，差别实在是太大了。
比如书本中要用到HttpRequestBase.java Tomcat8中就没有这个代码。
学习书本很不利。

那么直接用Tomcat4那套行吗？
servlet和catalina都是现成的。这个问题在于JDK1.4现在已经很少了。
很多JAVA语法和我们现在也不一样了。

怎么解决呢？
我觉得比较好的做法是用tomcat4那套catalina/servlet，然后JDK用较为新的，比如JDK1.6
一旦有较老的JAVA语法，JDK1.6不支持，就修改一下tomcat4的代码。
因为我们看各个版本的tomcat，都需要JDK1.X起，而JDK一般是向下兼容的。
一旦有发现因为是Java语法问题，一般可以通过修改成新的JAVA语法解决。

所以，最终的解决方案是：
根据HowTomcatWorks官网的源码，

1.把lib目录下的jar包都引用进来(包括servlet.jar)
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks/lib/*

2.把源码目录下的catalina源码引用进来
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks/src/org/apache/catalina/

最后再根据编译结果调整一下。
主要报错是早期的Catalina会使用Enumeration这个类。
这个类貌似后续高版本的Java不支持了，所以报错。目前采取的办法是注释代码。

===============================问题4
启动chap4/BootStrap的时候就报错：

Exception in thread "Thread-3" java.lang.IllegalMonitorStateException

Exception in thread "Thread-1" Exception in thread "Thread-2" java.lang.IllegalMonitorStateException
	at java.lang.Object.wait(Native Method)
	at java.lang.Object.wait(Object.java:502)
	at com.nbcb.mytomcat.chap4.HttpProcessor.await(HttpProcessor.java:183)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:141)
	at java.lang.Thread.run(Thread.java:748)

这个百度一查就知道，是因为Httpprocessor.await()方法没有加synchronized关键字

===============================问题5
我用httpclient调用本地chap4/BootStrap启动的服务:
String url = "http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb";

发现报错：
Caused by: org.apache.http.ProtocolException:
The server failed to respond with a valid HTTP response
但是这个服务用浏览器访问是正常的

从错误堆栈来看，应该是返回报文的http header有问题。
明天和正常的报文比对一下。看有什么http header 没有传

为了定位问题，我启动本地的nginx服务，发布静态资源：
http://127.0.0.1/
发现无论是浏览器访问还是httpclient访问都没问题

看一下浏览器访问nginx服务的http header
Connection:keep-alive
Data:Sat, 14 Sep 2019 09:13:49 GMT
Etag:"59746037-264"
Last-modified:Sun, 23 Jul 2017 08:37:11 GMT
Server:nginx/1.13.3

我们尝试在MyTomcat中设置response http header
主要是在HttpProcessor.process()中添加了response httpheader :

((HttpServletResponse)response).setHeader("Connection","keep-alive");
((HttpServletResponse)response).setHeader("Server","MyTomcat-chap4");
((HttpServletResponse)response).setHeader("Date", FastHttpDateFormat.getCurrentDate());
同时，添加了这个：
response.finishResponse();
用于返回response httpheader。
但是浏览器实际调用返回结果为：

<html>
...
</html>
HTTP/1.1
 200 OK
Content-Type: text/html
Server: MyTomcat-chap4
Connection: keep-alive
Date: Sat, 14 Sep 2019 10:21:05 GMT

但是奇怪的是，客户端浏览器还是接收不到response http headers，httpclient还是报错

怎么办呢？
后续计划通过plain Java networking的方式，看看，程序返回的和Nginx返回的内容究竟有什么不一样
plain Java networking的方式也没法看出来什么问题。
没啥用。

后续我们试试ackRequest(output)方法，试试先返回200状态，会不会好一点

我们试了ackRequest()方法，果然有效果！
我们通过浏览器访问，能够取到http header了，httpclient访问也不会报错。
但是服务端报错：
getWriter() has already been called for this response
这个好像是因为HttpServletResponse调用getWrite()方法问题引起的，具体原因没深入研究

解决方式是把之前注释的Response.getWriter()方法中的内容改回来就行了。

今天这个问题总算解决了，但是随之而来新的问题：
chap4/BootStrap.java启动后，第一次调用没问题，但是第二次调用报错：
org.apache.http.NoHttpResponseException: 127.0.0.1:8080 failed to respond
应该是服务端没有响应

===============================问题6




