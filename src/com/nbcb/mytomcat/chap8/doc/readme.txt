Loader
������Ҫ���ܵ��ǹ��ܸ�Ϊǿ���servlet loader


===============================Why customized Loader?
ΪɶҪ��һ���Զ����Loader��ԭ����SimpleLoader��ɶ���⣿
������˵��ԭ����SimpleLoader���ܹ��ڼ򵥣�ֻ�Ǹ��ݿͻ��������servlet name
���շ���ԭ���ʼ��servlet�ࡣ������ʵ�����л����У�������SimpleLoader��Ȼ��ԶԶ�޷�������Ҫ�ġ�
�Զ����Loader�����Ĺ��ܰ�����
1.reload
2.load cache
3.security manager

===============================Java����ص�ԭ��
�������Լ�����⣬Java����ص�ԭ���������ģ�
1.(��ǰͨ��������)��ĳ��Java�ļ������Class�ļ���
2.JVM��Class�ļ����н����������ɶ�Ӧ��Class����
3.���Ҫʵ����������󣬾͵���Class�����newInstance()�������������������ڴ����Դ��
tomcat�У���ν��load cache����ʵ�ǽ�Class�������˻��档������servlet�����ʵ����


===============================ֵ�ý���ļܹ�

1.reload����
2.cache����




===============================֮ǰSimpleLoader��˵��
Loader loader = new SimpleLoader();
context.setLoader(loader);

Load�ķ�ʽ�Ƚϼ򵥡�
������constructor�У���ʼ��һ��URLClassLoader����(��Ҫ��ָ��WebRoot·��)
Ȼ���ṩgetClassLoader()��������������container(SimpleWrapper)����������Servlet��

����Servlet��Ҳ��ֱ�ӣ����ǵ���load.loadClass(servletName)

��Ȼ������ǵ���Loader����servlet�Ļ�����ܣ������µ�Loader����Ҳ�����������ܣ����ǻ����һЩ�µ���Ҫ����
===============================����Load�¹���˵��
�µĹ��ܰ�����

1.������ȫ�ԣ�
  ����ֻ�ܼ���webroot�µ�servlet�࣬���������Ƽ���JVM����������
  ��ע����WebappClassLoader.setPermissions()������ʵ��Ȩ������
2.֧�ֶ�̬reload�Ĺ���
  �첽�̣߳��ᶨʱ���webroot���Ƿ���class����
  ��ע������첽�̶߳�����WebappLoader.run()�У�
  ������Context������WebappLoader���첽�̣߳�ʵ��servlet���reload
3.ͨ��cache���ƣ�����servlet����Ч��
  ��ע����WebappClassLoader��ʵ��cache����
  ֮ǰ���ع���servlet���󱣴���ResourceEntry������










