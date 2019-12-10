Chap5 Container


==================Chap5引入的概念

这章引入了Container的概念
Container相当于是容器，是一个接口
这个接口有四个子类：
Context/Host/Wrapper/Engine

当然这四个子类也是接口

那么具体的实现类是什么呢？
StandardContext
StandardHost
StandardWrapper
StandardEngine

具体的Container类图请参考visio

引入了Pipeline的概念
Pipeline是流水线，意思就是按照一定的顺序执行一连串的任务
某个Container会触发Pipeline
每个Pipeline包含一些子任务：Valve


Wrapper Interface
Wrapper作为容器的一种主要是用于处理一个单独的Servlet实例，执行Servlet中的各个方法
主要的方法是allocate()/loadServlet()
主要是用于初始化Servlet实例


Context
Context也作为一种容器，相当于WAS中的application，包含多个Wrapper(Servlet)

==================思考：为啥要建立这样一套架构
为啥要发明这么多概念呢？


==========================client url
http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb
http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb













