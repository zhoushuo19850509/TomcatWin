本章书本阅读


这章在第三章的基础上，改进了Connector的功能，具体表现在：

1.支持Http1.1新特性
这章的DefaultConnector,支持Http1.1新特性：
Persistent connections
Chunked Encoding

2.支持多线程的客户端链接
新的Connector支持多线程
之前第三章的HttpConnector，仅支持单线程对外提供服务。
这当然是不够的，因为同一时间只能为一个客户端请求服务
这个功能类似WAS中的webcontainer的概念。
后续我们还要再继续优化，因为新起一个线程来处理一个客户端请求，还是太重量级了。
(新起线程就意味着分配新的内存资源给Java，新起一个线程需要占用多少内存资源，可以计算出来)
这个可以参考Nginx/Netty的方案，利用epoll的原理，将原来阻塞式的处理，改为非阻塞式的异步处理。
大大提升并发处理的效率。

3.