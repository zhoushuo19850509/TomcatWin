Loader
这章主要介绍的是功能更为强大的servlet loader


===============================Why customized Loader?
为啥要搞一个自定义的Loader？原来的SimpleLoader有啥问题？
总体来说，原来的SimpleLoader功能过于简单，只是根据客户端请求的servlet name
按照反射原理初始化servlet类。但是在实际运行环境中，这样的SimpleLoader显然是远远无法满足需要的。
自定义的Loader新增的功能包括：
1.reload
2.load cache
3.security manager

===============================Java类加载的原理
按照我自己的理解，Java类加载的原理是这样的：
1.(提前通过编译器)将某个Java文件编译成Class文件；
2.JVM将Class文件进行解析，解析成对应的Class对象；
3.如果要实例化这个对象，就调用Class对象的newInstance()方法，给这个对象分配内存等资源；
tomcat中，所谓的load cache，其实是将Class对象做了缓存。而不是servlet对象的实例。


===============================值得借鉴的架构

1.reload机制
2.cache机制




===============================之前SimpleLoader简单说明
Loader loader = new SimpleLoader();
context.setLoader(loader);

Load的方式比较简单。
就是在constructor中，初始化一个URLClassLoader对象(主要是指定WebRoot路径)
然后提供getClassLoader()方法，供后续的container(SimpleWrapper)调用来加载Servlet类

加载Servlet类也很直接，就是调用load.loadClass(servletName)

当然，这个是调用Loader加载servlet的基本框架，后续新的Loader基本也是沿用这个框架，但是会加入一些新的重要功能
===============================本章Load新功能说明
新的功能包括：

1.提升安全性：
  限制只能加载webroot下的servlet类，不能无限制加载JVM中其他的类
  备注：在WebappClassLoader.setPermissions()方法中实现权限设置
2.支持动态reload的功能
  异步线程，会定时检测webroot下是否有class更新
  备注：这个异步线程定义在WebappLoader.run()中，
  但是由Context来调用WebappLoader的异步线程，实现servlet类的reload
3.通过cache机制，提升servlet加载效率
  备注：在WebappClassLoader中实现cache机制
  之前加载过的servlet对象保存在ResourceEntry对象中










