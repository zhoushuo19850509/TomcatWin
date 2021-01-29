20191016
把这章基本框架都搭建起来了。
画了一下架构图(visio)

20191023

20191028
今天补充了
1.SimpleWrapper：如何加载Servlet
2.BasicValve : SimpleWrapperValve  如何触发Servlet

20191029
今天把整个流程理顺了：
从BootStrap开始，如何调用SimpleWrapper，并且执行Pipeline的各个valve实例。
明天计划把两个实际的Valve实例实现一下就行了(HeaderLoggerValve/ClientIPLoggerValve)

20191129
今天把Chap5的整个架构/调用流程又梳理了一遍。
果然有一段时间没有看到代码，还是有点生疏了。

20191130
加入了Mapping的功能，能够根据客户端URL自动映射到对应的Wrapper处理Servlet

201911203
调试了BootStrap1.java

20191209
调试BootStrap2.java

20191210
提交chap5的代码到github










