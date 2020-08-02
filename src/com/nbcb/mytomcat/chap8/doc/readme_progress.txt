

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

接下来，就可以开始研究reload了

20200726
今天开始研究reload
今天把reload代码搞定了，很久成就感！
但是碰到一个问题，就是我们开发的tomcat，加载的servlet貌似有点问题，好像不是从WEB-INF/classes目录下取的

20200730
今天继续定位class类的问题，怎么解决呢？
这个没啥好说的，就是单步调试呗
大致问题应该找到了，其实就只要看一下我们加载的servlet类的类路径就行了：
日志：
class resource of the servlet instance: file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/

相关代码：
SimpleWrapper.loadServlet()

这个问题可能由来已久，之前我们用SimpleLoader加载Servlet的时候，问题就已经在了。
问题根源还是反射的基础不够扎实。
我们就单独就反射这块，补强一下，
搞一个测试类验证一下class文件的加载


20200801
测试类为：ClassLoaderTest.java
这个测试类加载jar包中的class文件和普通文件路径下的class文件都成功了

接下来可以尝试通过一个测试程序模拟
WebappLoader.createClassLoader()创建WebappClassLoader实例的代码，看看实际的Loader效果如何

目前我们的问题是，
我们加载的servlet Class ，getResource()的结果为：
file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/
执行结果也是和这个getResource()结果一样，都是执行这个out目录下的class类:ModernServlet.class

这显然和我们的预期不符
我们的预期是，
我们加载的servlet Class ，getResource()的结果为：
file:/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes/
要执行这个目录下的class类:ModernServlet.class


哈哈，问题解决了！
同时，也解决了reload测试的问题。
开心开心开心！
虽然之前重启tomcat的过程很痛苦，但是重新上手之后，还是很有收获的！
尤其是这个Loader问题的解决，学习了java load的原理，非常开心！


趁热好好总结一下。


