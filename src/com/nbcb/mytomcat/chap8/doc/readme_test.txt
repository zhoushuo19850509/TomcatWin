
这章新增了reload的功能，如何进行验证呢?
我们的设想是这样的，先默认加载一个servlet，然后我们用一个新的servlet来替换原来的servlet
看tomcat能否自动热加载(无需重启BootStrap)



1.验证基础Load功能
具体测试步骤为：
(1)启动本地tomcat工程： BootStrap.java
(2)输入URL访问本地tomcat servlet服务
http://localhost:8080/servlet/PrimitiveServlet

2.验证自动reload功能


3.验证cache功能


