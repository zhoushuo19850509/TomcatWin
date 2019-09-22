Chap3 Connector

这章介绍Connector的原理：

==========================================本章和之前的区别
Connector和之前的Java容器有啥不一样呢？
其实原理还是差不多的，无非就是启动一个ServerSocket，然后监听来自客户端的请求。
解析完客户端请求后，实例化一个本地的servlet类，进行处理，处理完后把结果返回给客户端。

和之前不同之处有：
1.Connector是启动一个新的线程，通过ServerSocket来监听客户端请求；

2.这次的Request/Response和之前的不太一样。之前是实现了ServletRequest/ServletResponse
这次是实现了HttpServletRequest/HttpServletResponse
那这个和之前有什么不一样呢？
这次包含了更多HTTP请求的信息，包括：Http header/Cookies from client/parameters from url

3.可以看到，这章很多业务逻辑，都放到Prcessor类了，而不像之前，放在Request/Response类中


==========================================客户端静态资源请求(来自浏览器)

我们从浏览器发起一个静态资源请求：
http://127.0.0.1:8080/index.html

服务端接收到的内容为：
GET /index.html HTTP/1.1
Host: 127.0.0.1:8080
User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Connection: keep-alive
Upgrade-Insecure-Requests: 1

我们看到，内容非常丰富
除了静态资源：index.html
还有http header
本章，我们就要解析来自客户端的http header/Cookies/session id/


==========================================客户端动态资源请求(来自浏览器)
http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb

服务端接收到的内容为：
GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
Host: 127.0.0.1:8080
User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:66.0) Gecko/20100101 Firefox/66.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Connection: keep-alive
Upgrade-Insecure-Requests: 1
Cache-Control: max-age=0


parsed url: /servlet/PrimitiveServlet
servletName: PrimitiveServlet
repository: file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/


我们看到，内容非常丰富
除了客户端要请求的servlet： PrimitiveServlet
还有parameter： name=zhoushuo&company=nbcb
还有cookies等字段(cookie需要客户端上送)


==========================================心得体会
这章开始明显开始有点吃力。为啥呢？因为这章的代码是不全的。
很多类只是用到了，但是没有实现。
别怕！
热心网友已经把完整代码贴出来了。参考：
https://github.com/Aresyi/HowTomcatWorks.git

我们已经clone到本地了：
$HOME/Document/testGit/HowTomcatWorks

我们随时可以参考这里的代码。


==========================================关于本章的难度
我们从git提交的频率来看，第三章难度确实比之前两章有了非常高的提升。
虽然是大致完成了这章的核心任务。
但是还有很多地方不明白。
现在还是先了解整体框架吧，看后续能否补全之前漏下的知识点。














