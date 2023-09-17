============================================源码示例1
中文译者的gitHub地址
https://github.com/Aresyi/HowTomcatWorks

我已经clone到本地了
/home/zhoushuo/Documents/testGit/HowTomcatWorks

============================================源码示例2
《How Tomcat Works》原版书也有源码：
https://www.brainysoftware.com/book/9780975212806;jsessionid=0DE37520BC120ACCD65D7F8D5348EB59

我也下载了放到本地了：
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks

============================================源码示例3
Tomcat各个版本的代码，下载到本地：
/home/zhoushuo/Documents/Tech/tomcat/TomcatCode



============================================我自己代码的gitHub地址
https://github.com/zhoushuo19850509/TomcatTest
https://github.com/zhoushuo19850509/TomcatWin

============================================代码说明
主入口:
HttpServer.java

chap1 A Simple Web Server
就像题目说的一样，这章主要是创建了一个简单的web server
我们启动HttpServer.java之后，客户端能够通过如下url:
http://localhost:8080/index.html
获取到一个静态页面

============================================源码阅读技巧
我们下载了各个版本的源码，包括<How Tomcat Works>官网源码、Tomcat官网源码等。
如何阅读呢？我们还是建议通过IDEA进行阅读。
功能丰富，阅读代码方便，支持搜索等功能。
但是需要注意:
1.要设置正确的Module
Module/sources

2.要设置准确的JDK
各个版本的Tomcat有对应的JDK版本，参考 readme_tomcat_version.txt


