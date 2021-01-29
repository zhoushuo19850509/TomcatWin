Chap2 A Simple Servlet Container
一个简单的Servlet容器，能够处理来自客户端的servlet请求

=====================================================特别说明
为啥说这个容器是简单的呢？
1.
因为这里执行的servlet是我们自己定义的(PrimitiveServlet.java)
而不是从war包自动解析的。
2.
这里执行的servlet需要我们手工把servlet对应的class文件放到webroot目录下
因为目前我们还不掌握Class loader的技术
目前我们要访问到PrimitiveServlet.java这个Servlet，
只能先把PrimitiveServlet.java放到src根目录下(不能放到src目录下的package各个层级，否则读取不到)
然后手工编译这个java文件，最后把编译好的class文件手工放到webroot目录下。

=====================================================代码说明
chap2这个package下的代码：
PrimitiveServlet.java 我们自定义的servlet
客户端正是要请求这个servlet的执行结果。
ServletProcessor1/ServletProcessor2分别是两个版本的HttpServer处理客户端servlet请求的处理类
ServletProcessor1做的事情主要是解析客户端url的请求，然后根据这个请求解析出客户端要调用的servlet类
并将解析结果返回给客户端。

ServletProcessor2是在ServletProcessor1的基础上，在安全方面做了加固。
原理也很直接，担心客户端自己的Servlet，将ServletRequest/ServletResponse转化为Request/Reponse对象，
然后自作主张调用Tomcat Request/Reponse对象中自定义的方法，
比如：Request.parseUri()/Reponse.sendStaticResource()
解决方案也很直接，就是通过FacadeRequest/FacadeResponse访问ServletRequest/ServletResponse对象。


其他package下的代码：
Request.java
  为了能够处理servlet请求，需要实现ServletRequest interface
Response.java
  为了能够处理servlet响应，需要实现ServletResponse interface


