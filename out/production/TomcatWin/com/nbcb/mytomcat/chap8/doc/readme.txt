Loader
这章主要介绍的是功能更为强大的servlet loader


===============================Why customized Loader?
为啥要搞一个自定义的Loader？原来的SimpleLoader有啥问题？


===============================Java类加载的原理


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










