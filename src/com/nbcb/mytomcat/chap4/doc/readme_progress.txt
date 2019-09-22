发现每天进度比较慢
所以每天都要就一下工作进展

20190907
今天把编译的问题解决了。
具体参考《readme_problems.txt》/问题3

20190908
今天解决了一个编译问题
具体参考《readme_problems.txt》/问题4
目前的进度是:调试到了HttpProcessor.process()方法


20190910
今天调试到SimpleContainer了，servlet马上能够调通了。

20190911
今天了解了一下content chunked的概念。
把ModernServlet调试成功了。
明天计划把多线程这个模型验证一下。

20190913
今天写了一个httpclient，调用本地chap4 BootStrap发布的服务，
报了一个错。这个明天再解决一下。
Caused by: org.apache.http.ProtocolException:
The server failed to respond with a valid HTTP response
从错误堆栈来看，应该是返回报文的http header有问题。
明天和正常的报文比对一下。看有什么http header 没有传

明天在这个单线程的程序基础上，用多线程试试

20190914
在昨天的基础上，在返回报文中，增加了response http headers
同时，在HttpProcessor中，处理完servlet之后，调用response.finishResponse()将
response http headers返回给客户端。
但是奇怪的是，客户端浏览器还是接收不到response http headers，httpclient还是报错
今天在之前httpclient基础上，又写了一个java networking版本的客户端程序
很遗憾，这个程序也没啥用，对于定位问题没什么用。
但是今天有一个收获，那就是看到status的问题，明天试试

20190915
今天把这个问题解决了
Caused by: org.apache.http.ProtocolException:

但是随之而来另外一个问题，
具体请参考<readme_problems.txt 问题6>

20190916
扩内存

20190917
扩内存

20190918
扩内存

20190919
梳理代码：梳理chap4的代码流程

20190920
梳理代码：梳理chap4的代码流程

20190921
解决第一次访问成功，第二次访问不成功的问题
后续计划：
1.多线程模型，并发访问Tomcat服务端
2.支持http persistent connection

