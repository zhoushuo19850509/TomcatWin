

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

20200802
今天把loader顺序的问题总结了一下。
同时开始研究cache



20200803
今天把cache的顺序梳理了一遍
主要参考Tomcat官方的WebappClassLoader.java
记录在UML流程图中了

明天计划:
1.system(loader)怎么来的?
2.securityManager怎么来的?


20200808
今天思索了一下cache的原理，

20200809
还是继续探索cache的原理，主要是阅读
WebappClassLoader.findResourceInternal()的代码流程
为啥这块内容要看这么久呢？主要还是因为对Java 反射部分的内容理解比较肤浅。


20200902
又是将近一个月没有看源码了，罪过罪过
导致之前看的cache相关内容，又有点生疏了。
0830周末头疼
0823周末牙疼
0816周末不知道在干吗
看来良好的身体才是革命的本钱。
先把cache的整个流程再通过UML图梳理一遍。

20200929
今天终于领悟了tomcat servlet cache的奥义
简单总结一下就是，如果没有cache，每次根据客户端请求，都要加载一遍servlet类
这个加载过程看起来很方便：
Class myClass = classLoader.loadClass(servletName);
servlet = (Servlet)myClass.newInstance();
核心是利用反射的原理，将class类文件转化为对象实例。
具体代码参考SimpleWrapper.loadServlet()

但是，这个过程的本质是什么呢？就是从文件系统中获取class类文件，然后对class类进行加载。
这个加载过程我们深入研究一下，其实还是很复杂的，要解析class文件，然后加载到内存加以执行。
显然，这个过程会拖慢tomcat加载servlet的效率。
tomcat作为java容器，对于servlet laod这块功能，显然是需要优化的。

怎么优化呢？根据servlet cache的思路，就是把那些曾经加载过的servlet类，放到内存中。
下载客户端再来请求类似的servlet，就能直接从内存中拿到servlet对象实例了。

这个思路非常不错。后续可以应用到项目实践中去。之后碰到类似的场景，可以用上。

当然，能够有这样的领悟还是很不容易的，这次花了这么久的时间才领悟到load的原理，
一方面是对java基础知识掌握不牢固(反射的原理，JVM加载Java class类的原理)
另一方面，servlet laod这块内容确实应该是tomcat最核心最难的部分了。应该和之前Connector并列最难的部分





通过这次领悟的过程，收获还是很大的：
1.成为Java大神的路还很长很长，要学习的内容还很多很多；
2.只有自己领悟的东西才是自己真正掌握的，其他的资料再丰富再详细，没有自己的领悟，是没用的；
3.学习源码、练习源码好处实在是太多了，学习源码需要有良好的编程语言支持，反过来，学习源码会促进对编程语言的深入掌握
4.所谓的学而不思则罔、思而不学则殆。学思结合是非常重要的；
你学习了很多知识，但是没有自己深入思考，是没啥印象的；
你平时思考了很多，想要做这做那，但是懒得去学习新的知识，没有深入实践，那也是没有用的；


20201001
理解了load cache原理之后，后续计划：
1.
一方面参考一下tomcat servlet load官方实现源码(包括较老的4版本和较新的9版本)，自己把cache功能实现了：
org.apache.catalina.loader.WebappClassLoader.java/findResourceInternal()

2.
另一方面，通过测试案例，比对一下，用了cache和没用cache的区别。
我们大致想想就知道，用了cache，性能肯定比不用cache要好。关键是好多少的问题。
如果没有用cache，那加载一个servlet类就需要访问tomcat本地磁盘，加载sevlet class文件。
如果用了cache，直接从内存中去拿就行了。关键是如何体现出差异来。
那么如何通过我们的程序，检验cache的性能提升了多少呢？

3.
最后还需要对Load这块内容好好总结一下。

Q:这边我有一个疑问，为啥官网的实现要这么做，把class的binary作为缓存。
直接把class类放到Map中不香吗？为啥非要放class类的binary？
这个Map的key/value为：servlet path/对应的servlet Class类
A：只要仔细看源码就能知道，Map中的key/value确实为servlet name/Class
后续每次客户端来请求同一个servlet类，都会从缓存中根据servlet name获取对应的Class类

20201009
国庆期间一直在思考，如何通过小程序，验证cache的性能作用。
总结了以下几点：
1.框架
比如Spring MVC框架，通过加载SpringDispatch来验证cache的性能

2.加载lib
servlet类，很可能会需要用到第三方jar包.
如果把这些jar包也提前加载进来，那么，cache的作用是否会更加明显？

3.加载一个比较大的servlet类
这个servlet类的文件比较大。

4.并发加载servlet
模拟在高并发、频繁调用servlet类的场景下，cache的作用

以上这些想法，其实都可以通过小程序来模拟。因为tomcat的本质就是load一段java程序，然后执行。
没有什么特别神秘的东西。如果要深入了解Load原理，当然要了解JVM的机制。
具体小程序可以参考这个package下的程序：com.nbcb.mytomcat.test.load.LoadTest
这个程序通过比较各种创建对象实例的方式，非常清晰地显示了load cache的性能优势。
对于Tomcat来说，这点性能提升是非常有用的。


临时感想：
对技术要永远保持敬畏。
就像我们学习tomcat，大致想想么，不就是处理客户端http请求，然后执行一段servlet，然后返回结果。
但是只要我们深入学习tomcat，会发现不是这么简单。
单单是load这章，就需要掌握JVM加载Java class文件的相关知识。
需要通过了解JVM虚拟机规范，来真正理解tomcat load原理，这是始料未及的。
就像我们游览一个地方，总是要亲自身临其境，才能真正去感受这个地方。
如果只是通过照片、电视去了解，那就流于形式了。
所以，学习一门知识，也是一样。尤其技术方面，实践是必不可少的。只有真正动手去做，才能体会这门技术。

20201021
既然搞清楚了load cache的好处，那么接下来就开始用代码实现了。
在写代码之前，我们先要规划一下。
我们知道，在实现load cache之前，我们的load原理可以参考SimpleWrapper.loadServlet()方法，具体Load逻辑：
ClassLoader classLoader = loader.getClassLoader();
myClass = classLoader.loadClass(servletName);
servlet = (Servlet)myClass.newInstance();

其中classLoader是我们WebappClassLoader.java
classLoader.loadClass(servletName)调用的是系统默认的java.lang.ClassLoader.loadClass()

为了实现load cache，我们计划对这个loadClass(String servletName)方法进行重载

具体的逻辑，我们至少要实现class load的功能。其他的功能，比如Load顺序、SecurityManage等高级主题，
看情况添加。


20201024
为了调试方便，后续我们建议对官网的tomcat工程做一下编译，根据官网的tomcat源码在本地运行。

今天，主要是搭建了load cache的框架
第八章结束后，必须好好总结一下，尤其是reload/load cache，这两块要好好梳理一下。
以后用到实际项目中去。

今日份的感悟：你看load这块，思考了这么长的时间。代码产出才多少行？
所以按照行数来判断软件的产出，那真是......
所以，思考是很重要的，思考各种原理，为什么呢？为什么呢？为什么呢？
还有就是提前知识储备很重要，如果没有《The Java Virtual Machine Specification》这本书，
估计也没法理解class load的概念。


Q&A
Q:
今天完成了load cache相关的代码，有一个小小的疑问：
既然我们的SimpleWrapper中把servlet实例缓存起来了(参考SimpleWrapper.instance)
那么为啥还有必要把servlet Class类缓存起来呢？
难道其他地方可能还会用到？

A:对于这个问题，我们只要在org.apache这个package下搜索loadClass关键字就行了
我们会发现，loader.loadClass()是一个通用的方法，不仅仅只是在Wrapper load servlet的时候会用到。
任何需要根据class path加载Class对象，最终获取对应实例的地方，都会用到loadClass()方法。
同时，loader的初始化也是独立的，具体初始化loader的代码可以参考：
WebappLoader.createClassLoader()
无非就是通过反射的方式，获取WebappClassLoader实例，
然后调用WebappClassLoader.addRepository()，设置一下对应的repository就行了。

当然这个解释是错的！我们仔细研究一下，就会发现，servlet类实例作为缓存，那是不可靠的。
很简单，因为不同客户端请求的servlet，servlet实例包含的数据显然是不同的！
不可能A发起的servlet调用和B发起的servlet调用，数据一模一样(至少clientIP不同)
所以后续要研究一下，客户端请求结束后，Wrapper对象应该会销毁，并且重新初始化。


Load这章要不先到这里吧。
后续我们找一个时间好好总结一下Load的整块内容

这章应该是目前为止，时间跨度最长的一章了：
20200625-20201024






