

20200625
已经有快半年没有看tomcat了，回顾一下tomcat的整体架构

20200626
梳理一下Loader相关的内容

20200627
今天梳理了一下之前Loader的逻辑，同时梳理了一下这章要新增的代码


20200706
今天梳理了一下之前tomcat的整体框架，主要是第五章SimpleContext的内容
具体可以参考doc/PIC/chap5_container.jpeg
后续可以通过我们的Mac下新的软件VisualDesigner重新绘制一遍chap6(LifeCycle)的内容

我们梳理一下Loader在整个tomcat中的位置
入口： bootstrap
  1.实例化一个Loader,：
    Loader loader = new WebappLoader();
  2.创建Context(StandardContext) 并且设置这个context的loader:
    context.setLoader(loader);
  3.当一个客户端http servlet请求过来时，由Context.invoke()来执行
    我们看Context.invoke()方法的实现：pipeline.invoke(request,response);
    最终是由pipeline来执行。
  4.那么执行servlet的pipeline，当然是basic pipeline，也就是SimpleContextValve
  5.我们看SimpleContextValve.invoke()，最终其实是由wrappler执行的
    wrapper.invoke(request,response);
    备注：整理的wrapper是由Mapper根据servlet name找到对应的wrapper
  6.我们看SimpleWrapper.invike()的实现：
    pipeline.invoke(request,response);  也是由pipeline来执行的
  7.那么我们找到SimpleWrapper的basic pipele:
    Servlet servlet = wrapper.allocate();
    是由SimpleWrapper.allocate()方法执行servlet类加载的
  8.我们看SimpleWrapper.allocate()方法的实现：
    调用的是SimpleWrapper.loadServlet()方法。loadServlet()方法中，根据servletName获取servlet实例的核心代码是这三行：
    Loader loader = getLoader();
    ClassLoader classLoader = loader.getClassLoader();
    myClass =    classLoader.loadClass(servletName);

    8.1 getLoader()获取Loader类
      我们看SimpleWrapper.getLoader()方法获取Loader的代码：
      parent.getLoader();  可见，SimpleWrapper是从他的parent处获取到Loader的
      也就是从SimpleContext处获取Loader，之前我们在Bootstrap中已经设置了Loader给StandardContext：WebappLoader
      也就是说，我们在SimpleWrapper中getLoader()得到的Loader是WebappLoader实例
    8.2 根据Loader获取ClassLoader
      我们看到，真正加载servlet实例的不是Loader，而是ClassLoader。那么CLassLoader是怎么来的呢
      上面的代码非常清楚，是根据loader.getClassLoader()获取到的
      那么我们看具体实现,可以看到WebappLoader.getLoader()方法仅仅只是一个getter()方法：return this.classLoader;
      真正的classLoader是在WebappLoader.start()的时候创建的(需要补充代码)
    8.3 由ClassLoader加载servlet实例

  11.WebappLoader.start()做哪些事情呢？其实就是调用WebappLoader.createClassLoader()
     创建一个WebappClassLoader实例
  12.最终，这个WebappClassLoader.loadClass(ServletName)方法来实现
    myClass = classLoader.loadClass(servletName);
    很明显，是通过调用WebappLoader.loadClass
    为了尽快调通Loader功能，我们建议可以先改造WebappClassLoader，使之能够实现loadClass(servletName)的功能
  13.还要搞清楚一个事情，就是Loader.start()或者说WebappLoader.start()是谁触发的？
     我们只要看一下chap6/SimpleContext.start()就知道了，Context.start()的时候，会把这个context体系中的实例，包括Loader/Wrapper等都初始化一遍。
     当然，这章我们使用StandardContext，但是原理是一样的。

20200714
隔了一个星期，再来复习一下Loader在整个Tomcat中的位置。

20200718
前面说了这么多，总结一下，我们要验证Loader的基础功能，还是先采用原来的SimpleClassLoader的方式
看看我们这次加入的



后续：
我们这次因为很久都没有碰tomcat了，导致我们花了很多时间，回顾tomcat的整体架构。有多方面的原因
一方面原因是tomcat整体结构确实很复杂，这个后续我们要一直保持学习tomcat，保证至少每星期学习一下，把整个tomcat架构时刻记在心中
同时，要画一个流程图，这个流程图是什么样的呢？要描绘出tcmcat接收到一个客户端servlet http请求，到最后调用servlet类处理客户端请求的整个流程

一方面是我们从windows迁移到Mac之后，没有合适的类似visio的画图工具，这个我们一直在找合适的工具，
一方面是我们目前的package结构。我们目前的结构是：每章会把这章增量的功能放到当前的package下，之前章节中已经开发好的内容，会复用
这样的架构带来的问题是，在定位之前代码的时候，可能会去好几个章节找。之前的章节可能会有多个实现，比如Context，Wrapper等等
之前很多章节都有。这样我们调试就非常麻烦。

为了解决这个问题，我们可能需要调整一下目前的架构。
把当前这种每章增量的架构，调整为统一的一个工程。后续有新增的功能，都往这个工程中添加。
这个我觉得可以作为整个课程学习完成后的一个课后作业：把各个章节的内容复习一遍，然后把各个章节的功能都放到一个整体的功能中去。

今天终于把Servlet调通了！！
这也意味着已经恢复状态了！！
从20200625到20200714，花了大半个月的时间才恢复状态，太不容易了！！！
后续要特别重视，持久保持状态。血的教训啊

20200719
既然SimpleLoader功能已经调试通了，就可以开始本章Loader的新功能了
WebappLoader.setRepositories() 调试成功
这个方法采用createClassLoader()方法来创建WebappClassLoader实例，
能够通过setRepositories()方法设置WEB-INF/classes和WEB-INF/lib
比之前dcreateSimpleClassLoader()功能丰富了很多

为了验证我们的成果，可以把原来的webroot目录下的servlet类删除：
cd /Users/zhoushuo/Documents/workspace/TomcatWin/webroot
mv ModernServlet.class ModernServlet.class_bak
mv PrimitiveServlet.class PrimitiveServlet.class_bak



