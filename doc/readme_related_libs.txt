tomcat依赖的lib包有那些呢？
从Tomcat官网可以找到答案：

比如最新的Tomcat9，需要依赖的lib包有：
* annotations-api.jar (Annotations package)
* catalina.jar (Tomcat Catalina implementation)
* catalina-ant.jar (Tomcat Catalina Ant tasks)
* catalina-ha.jar (High availability package)
* catalina-storeconfig.jar (Generation of XML configuration from current state)
* catalina-tribes.jar (Group communication)
* ecj-4.12.jar (Eclipse JDT Java compiler)
* el-api.jar (EL 3.0 API)
* jasper.jar (Jasper 2 Compiler and Runtime)
* jasper-el.jar (Jasper 2 EL implementation)
* jsp-api.jar (JSP 2.3 API)
* servlet-api.jar (Servlet 4.0 API)
* tomcat-api.jar (Interfaces shared by Catalina and Jasper)
* tomcat-coyote.jar (Tomcat connectors and utility classes)
* tomcat-dbcp.jar (package renamed database connection pool based on Commons DBCP 2)
* tomcat-jdbc.jar (Tomcat's database connection pooling solution)
* tomcat-jni.jar (Interface to the native component of the APR/native connector)
* tomcat-util.jar (Various utilities)
* tomcat-websocket.jar (WebSocket 1.1 implementation)
* websocket-api.jar (WebSocket 1.1 API)

再看看老版本的tomcat4:
* activation.jar (JavaBeans Activation Framework 1.0.2)
* ant.jar (Apache Ant 1.7.0)
* commons-collections.jar (Commons Collections 3.2)
* commons-dbcp.jar (Commons DBCP 1.2.2)
* commons-logging-api.jar (Commons Logging 1.1.1)
* commons-pool.jar (Commons Pool 1.4)
* jasper-compiler.jar (Jasper 2 Compiler)
* jasper-runtime.jar (Jasper 2 Runtime)
* jdbc2_0-stdext.jar (JDBC 2.0 Optional Package, javax.sql.*)
* jta.jar (Java Transacation API 1.1)
* mail.jar (JavaMail 1.3.3)
* naming-common.jar (JNDI Context implementation)
* naming-factory.jar (JNDI object factories)
* naming-resources.jar (JNDI DirContext implementations)
* servlet.jar (Servlet 2.3 and JSP 1.2 APIs)

从新老版本的jar包对比来看
1.新版本的jar包对原有的jar包进行了封装
比如commons-dbcp.jar封装成了tomcat-jdbc.jar
其实tomcat-jdbc.jar 就是封装了commons-dbcp.jar

2.版本是在不断升级的
比如servlet.jar从2.3升级到了4.0

3.不断加入了新功能
通过jar包可以看到，不断有新功能加进来
比如，我们看新版本中有websocket-api.jar
很明显是加入了websocket的功能