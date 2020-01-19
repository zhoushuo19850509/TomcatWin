Logger

Logger这章应该是目前为止最简单的一章了。
一方面是因为Logger模块本来就很简单。
另一方面是因为我们在工作中用到Logger比较多，比较熟悉。

=====================Logger体系
Logger体系比较简单。
首先就是Logger接口
然后就是LoggerBase这个基类实现Logger接口
最后是LoggerBase下的三个子类：SystemErrLogger/SystemOutLogger/FileLogger
其中FileLogger最为常用。

=====================Logger体系
Logger体系的核心当然是FileLogger啦，因为比较简单，这里没有重写这个代码
直接就调用catalina中自带的FileLogger 对象了
后续可以仔细看看FileLogger 代码，看看日志打印的原理

======================FileLogger用法
用法和我们在log4j中稍微有点不一样。
log4j中是这样的：
Logger log = new Logger.getLogger(this);
log.error("Exception occur here...");

这里的FileLogger的用法是这样的
1.实例化FileLogger对象
FileLogger logger = new FileLogger();

2.然后设置FileLogger对象的属性
logger.setSuffix(...);
logger.setPrefix("");
...

3.然后把FileLogger对象，设置给某个需要使用Log的对象
context.setLogger(logger);

4.最后，在各个对象中使用FileLogger对象打印日志
logger.log("write sth here...");


======================后续1
比较log4j和Catalina FileLogger的异同点
当然毋庸置疑,log4j更加专业一点。后续我猜想Tomcat肯定也会用类似log4j这样的专业打印日志组件

======================后续2
我们在生产环境中发现一个打印日志的BUG：
如果有多个进程(Process)把日志打印到同一个文件中，那么会出现文件锁异常的问题。
我们知道，Tomcat支持多进程的模式，那么在多进程下，FileLogger是怎么处理的？
是否还是像本章这样,实例化一个FillLogger的全局对象，然后context.setLog()的方式？我想应该是不会的。



