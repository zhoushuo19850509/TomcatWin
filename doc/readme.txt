this project follows the famous book: <How Tomcat works>


============================================总体概念
关于什么是Tomcat
Tomcat其实就是Java容器，所谓的Java容器，和web server不一样的地方，就是能够执行Java代码。
并且将Java代码执行结果返回给客户端。

关于什么是Servlet
Servlet就是定义在服务端的Java类，这个Java类只要实现了javax.servlet.Servlet接口
客户端就能够直接通过HTTP服务来调用这个Servlet，服务端能够把这个Servlet的执行结果返回给客户端



============================================doc下各个readme_XXX.txt的分类


readme.txt   // 总体说明

已添加
readme_prepare.txt  // 在开发tomcat之前要做的准备工作
readme_tomcat_version.txt  // 各个版本的tomcat
readme_related_libs.txt  // tomcat依赖的lib包
readme_code.txt   // 源码说明
readme_tomcat_version.txt  // tomcat相关的版本，主要是特定版本的tomcat需要搭配特定版本的JDK/Servlet
readme_summary.txt   // 相关总结
readme_autoDeploy.txt    // 自动编译出可用的binary
thinking.txt  // 胡思乱想
readme_WhyTomcat.txt     // 总结一下为啥要学习Tomcat，学习tomcat的理由(有哪些收获)
readme_enhancement.txt   // 后续要加强的Java知识
readme_autoTest.txt      // 自动测试当前版本tomcat的功能
readme_futurePlan.txt    // 未来要做的一些事情

待添加
readme_contribution.txt  // 如何为社区贡献







