这个文档是关于自动编译
为啥要自动编译呢？
因为我们在实际运行中发现，要部署某个servlet比较麻烦。

ant需要做的事情是：
1.自动将整个工程进行编译

2.将工程中各个Servlet的编译结果放到webroot中去
这样客户端访问Servlet服务的时候，就能访问到了

3.自动启动我们指定的Servlet服务
一般是BootStrap.java