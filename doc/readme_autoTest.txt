关于自动验证Tomcat的功能。

后续我们肯定是持续优化Tomcat，并且学习Tomcat release的机制，不断发布新的版本。
那么，我们如何保证版本的可靠性呢？
我们知道Tomcat的功能是非常多的，如果要依靠手工验证，拿显然是不可能的。

所以，我们要参考TestUI框架，搞一个自动化验证的工具。能够自动对存量功能进行回归验证。


同时，我们可以参考MySQL官网上的一个Test Framework
了解像MySQL这样的大型开源软件是如何通过测试保证软件质量的
https://dev.mysql.com/doc/dev/mysql-server/latest/PAGE_MYSQL_TEST_RUN.html