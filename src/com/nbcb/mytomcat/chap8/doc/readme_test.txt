
����������reload�Ĺ��ܣ���ν�����֤��?
���ǵ������������ģ���Ĭ�ϼ���һ��servlet��Ȼ��������һ���µ�servlet���滻ԭ����servlet
��tomcat�ܷ��Զ��ȼ���(��������BootStrap)



1.��֤����Load����
������Բ���Ϊ��
(1)��������tomcat���̣� BootStrap.java
(2)����URL���ʱ���tomcat servlet����
http://localhost:8080/servlet/PrimitiveServlet
http://localhost:8080/servlet/ModernServlet


2.��֤�Զ�reload����

Ҫ��֤reload���ܵ�˼·Ϊ��
(1)����WEB-INF�·�һ��Servlet�࣬����ModernServlet.class
/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes/ModernServlet.class

(2)����MyTomcat�����Է������Servlet:
http://localhost:8080/servlet/ModernServlet

���Կ������Servlet���еĽ��

(3)Ȼ�������޸�Servlet��Ĵ��룺ModernServlet.java
(4)��������µ�Servlet�࣬����ModernServlet.class
(5)������±����class�า��WEB-INF�µ�class��
mv /Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/ModernServlet.class
/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes
������Ҫ�ر�ע�⣬����Ҫ��mv�����outĿ¼���±����class�ֱ࣬�Ӽ��е�WEB-INFĿ¼��
����Ļ������ǵ�Loader�����ȼ���outĿ¼�µ�class�࣬�����Java Loader˳���й�

(6)���ǲ�������MyTomcat��ֻҪ������һ����־�����ˣ���־����ʾ���ǵ��첽�̼߳�⵽WEB-INF�µ�class���б仯
���Զ�����Context�����¼������µ�servlet class��

3.��֤cache����


