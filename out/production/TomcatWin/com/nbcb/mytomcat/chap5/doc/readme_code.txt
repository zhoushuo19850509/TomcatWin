
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
