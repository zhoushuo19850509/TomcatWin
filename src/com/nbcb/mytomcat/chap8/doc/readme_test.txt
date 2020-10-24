
这章新增了reload的功能，如何进行验证呢?
我们的设想是这样的，先默认加载一个servlet，然后我们用一个新的servlet来替换原来的servlet
看tomcat能否自动热加载(无需重启BootStrap)



1.验证基础Load功能
具体测试步骤为：
(1)启动本地tomcat工程： BootStrap.java
(2)输入URL访问本地tomcat servlet服务
http://localhost:8080/servlet/PrimitiveServlet
http://localhost:8080/servlet/ModernServlet


2.验证自动reload功能

要验证reload功能的思路为：
(1)先在WEB-INF下放一个Servlet类，比如ModernServlet.class
/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes/ModernServlet.class

(2)启动MyTomcat，尝试访问这个Servlet:
http://localhost:8080/servlet/ModernServlet

可以看到这个Servlet运行的结果

(3)然后我们修改Servlet类的代码：ModernServlet.java
(4)编译这个新的Servlet类，生成ModernServlet.class
(5)把这个新编译的class类覆盖WEB-INF下的class类
mv /Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/ModernServlet.class
/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes
这里需要特别注意，我们要用mv命令把out目录下新编译的class类，直接剪切到WEB-INF目录下
否则的话，我们的Loader会优先加载out目录下的class类，这个和Java Loader顺序有关

(6)我们不用重启MyTomcat，只要静静看一下日志就行了，日志会提示我们的异步线程检测到WEB-INF下的class类有变化
会自动重启Context，重新加载最新的servlet class类

3.验证cache功能


