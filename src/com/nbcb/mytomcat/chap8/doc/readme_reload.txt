
这个文档主要是深入讨论reload相关内容

================================reload过程梳理
1.启动一个异步线程
WebappLoader本身就是一个异步线程，这个线程做的事情请参考代码：
WebappLoader.run()

2.循环校验servlet class是否修改
一个while循环，调用WebappClassLoader的modified()方法，来检测servlet class是否修改
WebappClassLoader.modified()方法的具体实现请参考：
org.apache.catalina.loader.WebappClassLoader.modified()

其实也比较简单，就是当初始化的时候，把tomcat servlet部署路径下的class类的信息收集起来。
其中一个信息叫做lastModifiedDate，就是最后修改的日期时间，然后把各个class类的lastModifiedDate信息放到一个数组中：
lastModifiedDates

后续modified()方法就会遍历tomcat servlet部署路径下的class类，将最新的lastModifiedDate和lastModifiedDates数组中对应的老时间进行比对。
一旦发现lastModifiedDate不一致(说明文件有更新)，就马上返回true


3.一旦发现servlet class有了修改，启动异步线程去处理
马上通知对应的context reload
调用WebappLoader.notifyContext()
这个方法很简单，就是启动一个单独的线程，去处理：
这个线程类就是这样一个内部类： WebappContextNotifier

需要特别注意，在调用notifyContext()异步线程通知context reloadd之后，需要马上跳出while循环。
避免反复reload

4.context reload
具体的reload逻辑请参考:
org.apache.catalina.core.StandardContext.java / StandardContext.reload()
我们看一下reload()方法的实现：
总体来说就是重启一下context
详细的流程是先把挂靠在context下的wrapper 都stop
然后再把这些wrapper start
具体逻辑要再看看

5.重启Loader
我们知道,在context.reload()方法中，会触发Loader的重新启动。
Loader重新启动，其实就是调用WebappLoader.stop()/start()方法
所以，真正的reload任务其实是在WebappLoader.stop()/start()方法中完成的。


================================reload的几个问题
问题1 既然reload()的动作是由WebappLoader这个异步线程来完成的。
那么，WebappLoader这个异步究竟是怎么启动的呢？
启动过程如下：
1.WebappLoader.start()中会调用threadStart()方法，启动WebappLoader本身的异步线程
2.一旦检测到modified，WebappLoader的线程就退出了
3.后续在StandardContext.reload()中，会通过调用WebappLoader.start()，重新启动WebappLoader异步线程

问题2 除了servlet class类，那些配置文件，比如web.xml文件、jsp文件、js文件，会不会实时reload呢？


问题3 在reload过程中，是否会影响tomcat正常对外服务？
如果不影响，tomcat是怎么做到的？

================================reload功能反思总结
1.将reload功能用于热加载的场景
reload功能在我们实际工作中应用非常广泛。
比如我们影像平台，为了管控后台服务，需要限制对于某个后台服务的访问。
怎么办呢？一种简单的思路就是在数据库中登记一张后台服务黑名单表，把需要限制访问的后台服务登记到这张表中。
客户端调用后台服务的时候，后台服务从数据库查看一下，当前服务是否在黑名单中。
这个方案有一个什么问题呢？就是客户端每次调用后台服务的时候，后台都要访问一次数据库。对数据库造成较大的压力。
改进的方案一个是增加数据库缓存、另一个方案是把数据缓存到redis。
这两个方案我觉得都不够优雅，因为我们每次在数据库修改黑名单数据后，需要刷新缓存。

一个优雅的方案就是把黑名单作为配置信息，然后采用reload的方式，用异步线程去定时获取最新的黑名单信息。

当然，网上还有其他热加载的方案(具体参考印象笔记：《改了配置，不想重启，怎么整？》)，但是我觉得这个方式实现是比较直接的。
只要用一个异步线程，就能够实现配置热加载。

================================reload流程图
这一整个流程最好画一下流程图