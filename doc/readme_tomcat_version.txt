我们知道，Tomcat版本是在不断演进的

不断有老的BUG被修复，同时不断有新的功能加进来

我们在实际模仿Tomcat代码写自己的Java容器过程中，发现Tomcat版本很重要。
同时Tomcat和servlet版本/JDK版本，都有对应关系。罗列如下：


Tomcat4对应JDK1.4  Servlet 2.3
Tomcat5对应JDK1.5  Servlet 2.4 API)
Tomcat6对应JDK1.5起 Servlet 2.5 API
Tomcat7对应JDK1.6起 Servlet 3.0 API
Tomcat8对应JDK1.7起 Servlet 3.1 API
Tomcat9对应JDK1.8起 Servlet 4.0 API


备注2：JDK和Servlet版本请参考tomcat官网 Download各个版本的release-note
如果更加深入一点，release-note还列出了各个版本tomcat依赖的lib包
后续如果编译的时候报错是缺少对应的lib包，就把对应tomcat版本的binary下lib包引用进来

一般来说，我们
下载某个版本的Tomcat源码，源码目录下有servlet源码。
下载Tomcat binary，lib目录一般会有对应的servlet lib包：servlet-api.jar

我们以Tomcat8为例：
Tomcat8源码目录：
/home/zhoushuo/Documents/Tech/tomcat/TomcatCode/
apache-tomcat-8.5.45-src/java/org/apache/catalina

对应的servlet源码目录：
/home/zhoushuo/Documents/Tech/tomcat/TomcatCode/
apache-tomcat-8.5.45-src/java/javax/servlet

对应的servlet lib包目录：
/home/zhoushuo/Downloads/apache-tomcat-8.5.23/lib/servlet-api.jar

那么，早期的Tomcat4的catalina/servlet/JDK 应该怎么弄呢？
我们选用的catalina是《How Tomcat Works》原书的官方源码：
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks/src/org/apache/catalina

选用的servlet是《How Tomcat Works》原书官方源码的lib目录：
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks/lib/servlet.jar





