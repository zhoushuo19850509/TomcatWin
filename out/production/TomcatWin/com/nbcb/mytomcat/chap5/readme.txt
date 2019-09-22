Chap5 Container


==================书本阅读

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

引入了Pipeline的概念
Pipeline是流水线，某个Container会触发Pipeline
每个Pipeline包含一些子任务：Valve


Wrapper Interface
Wrapper主要是用于处理一个单独的Servlet实例，执行Servlet中的各个方法
主要的方法是allocate()/loadServlet()
主要是用于初始化Servlet实例


Context


==================本章代码说明

SimpleWrapper    //an Implementation of Wrapper Interface
SimpleContext    // Context可以加载多个(子)Wrapper

SimplePipeline   //an Implementation of pipeline
SimpleWrapperValve  //
SimpleLoader     // 加载本地Servlet实例
SimpleContextMapper  // Mapper什么意思懂吧？就是我们之前定义在web.xml中的url中servlet名称和我们本地工程Servlet的对应关系

ClientIPLoggerValve // valve that print the client's ip
HeaderLoggerValve   // valve that print the client's http header

BootStrap1.java  // 启动代码示例1 演示了Wrapper(Servlet)/Pipeline/vavle/的使用方法
BootStrap2.java  // 启动代码示例2 演示了一个Context如何处理多个Servlet(Wrapper)














