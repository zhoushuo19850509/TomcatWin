this project follows the famous book: <How Tomcat works>


============================================总体概念
关于什么是Tomcat
Tomcat其实就是Java容器，所谓的Java容器，和web server不一样的地方，就是能够执行Java代码。
并且将Java代码执行结果返回给客户端。

关于什么是Servlet
Servlet就是定义在服务端的Java类，这个Java类只要实现了javax.servlet.Servlet接口
客户端就能够直接通过HTTP服务来调用这个Servlet，服务端能够把这个Servlet的执行结果返回给客户端

============================================源码示例1
中文译者的gitHub地址
https://github.com/Aresyi/HowTomcatWorks

我已经clone到本地了
/home/zhoushuo/Documents/testGit/HowTomcatWorks

============================================源码示例2
《How Tomcat Works》原版书也有源码：
https://www.brainysoftware.com/book/9780975212806;jsessionid=0DE37520BC120ACCD65D7F8D5348EB59

我也下载了放到本地了：
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks

============================================源码示例3
Tomcat各个版本的代码，下载到本地：
/home/zhoushuo/Documents/Tech/tomcat/TomcatCode



============================================我自己代码的gitHub地址
https://github.com/zhoushuo19850509/TomcatTest


============================================代码说明
主入口:
HttpServer.java

chap1 A Simple Web Server
就像题目说的一样，这章主要是创建了一个简单的web server
我们启动HttpServer.java之后，客户端能够通过如下url:
http://localhost:8080/index.html
获取到一个静态页面


============================================关于自动编译
为啥要自动编译呢？
因为我们在实际运行中发现，要部署某个servlet比较麻烦。

ant需要做的事情是：
1.自动将整个工程进行编译

2.将工程中各个Servlet的编译结果放到webroot中去
这样客户端访问Servlet服务的时候，就能访问到了

3.自动启动我们指定的Servlet服务
一般是BootStrap.java

=============================================为啥要学习tomcat源码？
1.了解HTTP1.1是怎么实现的
能够了解应用层的协议，比如HTTP1.1，是如何实现的。
后续通过学习更高版本的tomcat，可以了解HTTP2的实现原理

2.了解大型软件系统的设计思路




