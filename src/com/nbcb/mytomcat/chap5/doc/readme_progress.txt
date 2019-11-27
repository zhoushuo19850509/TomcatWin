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









