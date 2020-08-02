核心代码说明


重点代码就以下这些，两个接口，两个实现类：
Loader
Reloader

WebappLoader
WebappClassLoader

具体UML请参考：
/Users/zhoushuo/Documents/workspace/TomcatWin/doc/UML/Loader.umdx


下面我们着重来分析一下，两个核心实现类WebappLoader/WebappClassLoader具体要做哪些事情

=============================WebappLoader
主要实现如下功能：
1.创建真正的servlet loader类： WebappClassLoader
  参考WebappLoader.createClassLoader()

2.setRepositories
设置Servlet类存放的路径，比如之前SimpleLoader设置的Servlet路径为：
/Users/zhoushuo/Documents/workspace/TomcatWin/webroot/

当然，具体逻辑要比原来SimpleLoader要复杂，具体参考：
org.apache.catalina.loader.WebappLoader.setRepositories()

总体来说，这次setRepository要设置两个地址：
(1)servlet类存放的路径
WEB-INF/classes

(2)servlete需要引用的jar包路径
WEB-INF/lib



3.set class path
这个主要是为了设置JSP编译器的，这里暂时先不讨论

4.通过设置servlet访问权限，提升安全性

  具体请参考：
  org.apache.catalina.loader.WebappLoader.setPermissions()


5.通过异步线程实现servlet reload功能
  代码请参考：WebappLoader.run()
  文档请参考：readme_reload.txt
  当然，reload的流程可以再通过UML好好梳理一下

=============================WebappClassLoader
从总体来说，WebappClassLoader实现的功能和之前SimpleLoader差不多，
就是创建一个URLClassLoader类对象，供SimpleWrapper.loadServlet()调用，根据servlet名称加载servlet实例
当然，这里的class loader功能更为强大。

具体实现如下功能：
1.实现servlet class load功能
  实现方式和之前的SimpleLoader类似，主要是借助java.net.URLClassLoader实现加载
  所有加载的servlet class类信息放在这个类中ResourceEntry中。

具体加载步骤为：
(1)
(2)
(3)
(4)
(5)

具体逻辑参考代码：
org.apache.catalina.loader.WebappClassLoader.loadClass(servletName)

2.实现cache，提升servlet加载效率
  所有cache起来的servlet class对象实例保存在:
  Map<String ,ResourceEntry> resourceEntries = new HashMap<String, ResourceEntry>();

具体实现参考：
：org.apache.catalina.loader.WebappClassLoader.findResource()方法
这个方法会先尝试从resourceEntries中看一下当前要加载的class，之前是否已经加载过

3.检测servlet类有没有更新
这个功能主要是通过实现Reloader.modified()方法实现的
具体实现逻辑参考：org.apache.catalina.loader.WebappClassLoader.modified()方法

modified()方法的逻辑总结如下:
(1)WebappLoader启动的时候，先调用WebappClassLoader.setLastModifieds();方法，
   把repository(/WEB-INF/classes)下所有文件的lastModified字段
   收集起来，放到WebappClassLoader一个数据结构中去；
(2)后续WebappLoader会有异步线程，调用WebappClassLoader.modified()方法，
   检查repository(/WEB-INF/classes)下的文件，lastModified字段是否有修改；
(3)



4.提升安全性
就是限制某些类(某些package下的类)被加载
具体参考:
org.apache.catalina.loader.WebappClassLoader.filter()
packageTriggers


=============================StandardContext
和之前的SimpleContext不同，这章的StandardContext新增了reload()方法，主要是配合Loader,实现servlet的热加载


=============================ResourceEntry
ResourceEntry代表一个通过Loader加载进来的servlet资源
包括servlet的class类(binary code，用于caching)，上次修改的时间(用于reload)，servlet URL地址(类路径)


=============================代码入口BootStrap
代码入口依然是BootStrap
和之前几章不同，之前用于Load servlet类的是SimpleLoader.java ：

这章，要用功能更为强大的Loader:
WebappLoader.java