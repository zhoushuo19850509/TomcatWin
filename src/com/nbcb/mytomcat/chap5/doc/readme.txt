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

==================本章代码说明

BootStrap1.java  // 启动代码示例1 演示了Wrapper(Servlet)/Pipeline/vavle/的使用方法

SimpleWrapper    //an Implementation of Wrapper Interface
SimpleContext    // Context可以加载多个(子)Wrapper

SimplePipeline   //an Implementation of pipeline
SimpleWrapperValve  //
SimpleLoader     // 加载本地Servlet实例
SimpleContextMapper  // Mapper什么意思懂吧？就是我们之前定义在web.xml中的url中servlet名称和我们本地工程Servlet的对应关系

ClientIPLoggerValve // valve that print the client's ip
HeaderLoggerValve   // valve that print the client's http header


BootStrap2.java  // 启动代码示例2 演示了一个Context如何处理多个Servlet(Wrapper)














