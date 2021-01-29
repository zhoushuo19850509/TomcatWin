这个文档是关于自动编译
为啥要自动编译呢？
因为我们在实际运行中发现，要部署某个servlet比较麻烦。

ant需要做的事情是：
1.自动将整个tomcat工程进行编译

2.自动将我们servlet工程进行编译打包(成war包)

3.将Servlet工程的编译结果部署到tomcat工程中去

4.自动启动我们的Tomcat工程
一般是BootStrap.java

5.最后自动化验证
验证内容包括对Tomcat工程本身的功能验证；
对我们发布的servlet服务进行自动化验证


最终，客户端就能访问我们自己的Tomcat发布的Servlet服务了