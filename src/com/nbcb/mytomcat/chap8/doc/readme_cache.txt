
这个文档主要是详细说明Loader的cache原理

=======================================实现思路：
1.原来加载servlet class的方式
我们知道，Loader的功能，主要是加载servlet class
而加载的动作是Wrapper发起的：
SimpleWrapper.loadServlet()
SimpleWrapper会先看看当前要加载的servlet class实例是否存在(之前是否加载过)
如果没有加载过，就调用Loader(WebappClassLoader)，根据servletName加载servlet class实例：
myClass =    classLoader.loadClass(servletName);

2.getLoader()改造
这个步骤看起来没有问题，但是我们仔细想想，如果每个serlvet都等到客户端来调用的时候才去加载，拿加载效率就有点低
如果我们能够提前加载好，放到cache里，就能大大加速客户端调用servlet的效率了。

这里我们看到，最终获取servlet class实例还是由 WebappClassLoader.getLoader()实现
之前我们的getLoader方法是是由WebappClassLoader的父类实现的，现在我们为了实现cache的功能，重载这个方法

3.如何实现缓存
至于如何实现缓存，我们可以参考Tomcat官方的文件：
org.apache.catalina.loader.WebappClassLoader.java
这里详细介绍了Loader如何利用缓存提升加载效率

======================================具体实现步骤：
首先，cache功能在WebappClassLoader中实现



======================================实现原理：
对于cache的原理，我也一直在思索，


======================================代码参考：
1.参考Loader根据servletName加载servlet class的入口方法：
org.apache.catalina.loader.WebappClassLoader.loadClass()

2.从缓存获取cache的方法：
org.apache.catalina.loader.WebappClassLoader.findResource()

======================================备注：
对于cache功能，我想WAS中应该也会有。
突然想到，我们之前用jarjar方案，避免jar包冲突。这个方案可能会有问题，因为cache方案会把加载过的class缓存起来
那么，jarjar方案就会存在内存使用过高的问题。


======================================问题
1.我这边有一个疑问
既然要加载的servlet在Wrapper中已经缓存了一份(参考SimpleWrapper.loadServlet())，那为啥我们还要开发cache功能呢？
我们在WebappClassLoader中缓存的那些loadedClass，在Wrapper中不是有吗？那么请问，缓存的意义何在？

======================================讨论cache对性能提升有多少帮助
要讨论cache对tomcat servlet load性能提升有多少帮助，还是要用数据说话。

我们要比对的对象是：
1.原来通过URLClassLoader方式，即每次加载servlet class类
2.通过新的cache方式，提前将servlet class加载到内存中，放到某个Map中。

需要验证的场景为：
1.循环调用servlet类
2.并发调用servlet类
3.调用简单的servlet类、调用复杂的servlet类



======================================总结：
从总体来看，Loader的作用，是根据servlet name,返回一个Class对象

