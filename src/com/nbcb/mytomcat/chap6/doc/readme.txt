Chapter6 Lifecycle

Lifecycle主要是管理servlet的生命周期。有啥用呢？
总结一下，就是在我们启动、停止各种container对象的时候，能够触发对应的监听类。

相对前面几章，这章应该算是非常简单了。
但是要搞清楚Listner的机制，能够灵活运用，融汇贯通，那要花费更多的时间了。



==========================这章的要点
1.各个层次(比如Context/Wrapper/Pipleline)，都可以通过实现Lifecycle类
做到start()/stop()的时候都执行一段代码

2.引入了事件监听的概念。
这个事件监听的概念和Java UI中那些触发UI的监听事件类似。
类似的用法还有C#中的事件定义/安卓iOS中的click事件定义等等。

3.这章的官方代码中,SimpleContext实现了Lifecycle接口
后来我自己仿照着做，SimpleWrapper也实现了Lifecycle接口。

4.这样核心的代码是LifecycleSupport
这个文件是自己实现的，敲一遍这个代码，就能够搞清楚整个监听事件的原理，
能够搞清楚我们之前定义的Listener监听类(SimpleContextLifecycleListener)是如何注册、如何被触发的

4.当然，启动的入口还是BootStrap2
在BootStrap2中需要定义监听类：比如SimpleContext的监听类：SimpleContextLifecycleListener
还要在BootStrap2中启动SimpleContext，这样SimpleContext这个Container以及他的child container都启动了。
SimpleContext启动的时候，会出发监听类对应的事件方法：SimpleContextLifecycleListener


==========================这章几个重要的概念要搞清楚：
1.Lifecycle接口
要进行生命周期管理的对象，就要实现Lifecycle这个接口

2.LifecycleListener接口
要设置某个container对象的监听类，就要实现这个接口

3.LifecycleEvent对象
生命周期管理事件，事件有很多状态类型，定义在Lifecycle中

==========================client url
http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb
http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb