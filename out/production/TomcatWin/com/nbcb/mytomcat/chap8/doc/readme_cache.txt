
����ĵ���Ҫ����ϸ˵��Loader��cacheԭ��

=======================================ʵ��˼·��
1.ԭ������servlet class�ķ�ʽ
����֪����Loader�Ĺ��ܣ���Ҫ�Ǽ���servlet class
�����صĶ�����Wrapper����ģ�
SimpleWrapper.loadServlet()
SimpleWrapper���ȿ�����ǰҪ���ص�servlet classʵ���Ƿ����(֮ǰ�Ƿ���ع�)
���û�м��ع����͵���Loader(WebappClassLoader)������servletName����servlet classʵ����
myClass =    classLoader.loadClass(servletName);

2.getLoader()����
������迴����û�����⣬����������ϸ���룬���ÿ��serlvet���ȵ��ͻ��������õ�ʱ���ȥ���أ��ü���Ч�ʾ��е��
��������ܹ���ǰ���غã��ŵ�cache����ܴ����ٿͻ��˵���servlet��Ч���ˡ�

�������ǿ��������ջ�ȡservlet classʵ�������� WebappClassLoader.getLoader()ʵ��
֮ǰ���ǵ�getLoader����������WebappClassLoader�ĸ���ʵ�ֵģ���������Ϊ��ʵ��cache�Ĺ��ܣ������������

3.���ʵ�ֻ���
�������ʵ�ֻ��棬���ǿ��Բο�Tomcat�ٷ����ļ���
org.apache.catalina.loader.WebappClassLoader.java
������ϸ������Loader������û�����������Ч��

======================================����ʵ�ֲ��裺
���ȣ�cache������WebappClassLoader��ʵ��



======================================ʵ��ԭ��
����cache��ԭ����Ҳһֱ��˼����


======================================����ο���
1.�ο�Loader����servletName����servlet class����ڷ�����
org.apache.catalina.loader.WebappClassLoader.loadClass()

2.�ӻ����ȡcache�ķ�����
org.apache.catalina.loader.WebappClassLoader.findResource()

======================================��ע��
����cache���ܣ�����WAS��Ӧ��Ҳ���С�
ͻȻ�뵽������֮ǰ��jarjar����������jar����ͻ������������ܻ������⣬��Ϊcache������Ѽ��ع���class��������
��ô��jarjar�����ͻ�����ڴ�ʹ�ù��ߵ����⡣


======================================����
1.�������һ������
��ȻҪ���ص�servlet��Wrapper���Ѿ�������һ��(�ο�SimpleWrapper.loadServlet())����Ϊɶ���ǻ�Ҫ����cache�����أ�
������WebappClassLoader�л������ЩloadedClass����Wrapper�в���������ô���ʣ������������ڣ�

======================================����cache�����������ж��ٰ���
Ҫ����cache��tomcat servlet load���������ж��ٰ���������Ҫ������˵����

����Ҫ�ȶԵĶ����ǣ�
1.ԭ��ͨ��URLClassLoader��ʽ����ÿ�μ���servlet class��
2.ͨ���µ�cache��ʽ����ǰ��servlet class���ص��ڴ��У��ŵ�ĳ��Map�С�

��Ҫ��֤�ĳ���Ϊ��
1.ѭ������servlet��
2.��������servlet��
3.���ü򵥵�servlet�ࡢ���ø��ӵ�servlet��



======================================�ܽ᣺
������������Loader�����ã��Ǹ���servlet name,����һ��Class����

