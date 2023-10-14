关于版本管理的思考

我们tomcat demo的版本管理策略已经变了很多次了。

从TomcatTest到TomcatWin到SimpleTomcat
一直在调整

1.单独工程模式
TomcatWin是单工程模式
各个chapter的更新都放在一个工程中

good part:
(1)管理方便

bad part:
(1)


2.多工程模式
SimpleTomcat是多工程模式
每个chapter一个单独的工程

good part:
(1)每章改动的内容都非常清晰

bad part：
(1)这和我们实际项目开发中的场景不符
实际中一般是新拉一个分支，而不是开辟很多新的工程。
(2)合并代码不方便
(3)自动化部署不方便
我们自动化部署的时候一般是配置某个git 地址
如果把很多工程塞在一个目录下，显然不适合自动化部署

