

20200625
�Ѿ��п����û�п�tomcat�ˣ��ع�һ��tomcat������ܹ�

20200626
����һ��Loader��ص�����

20200627
����������һ��֮ǰLoader���߼���ͬʱ������һ������Ҫ�����Ĵ���


20200706
����������һ��֮ǰtomcat�������ܣ���Ҫ�ǵ�����SimpleContext������
������Բο�doc/PIC/chap5_container.jpeg
��������ͨ�����ǵ�Mac���µ����VisualDesigner���»���һ��chap6(LifeCycle)������

��������һ��Loader������tomcat�е�λ��
��ڣ� bootstrap
  1.ʵ����һ��Loader,��
    Loader loader = new WebappLoader();
  2.����Context(StandardContext) �����������context��loader:
    context.setLoader(loader);
  3.��һ���ͻ���http servlet�������ʱ����Context.invoke()��ִ��
    ���ǿ�Context.invoke()������ʵ�֣�pipeline.invoke(request,response);
    ��������pipeline��ִ�С�
  4.��ôִ��servlet��pipeline����Ȼ��basic pipeline��Ҳ����SimpleContextValve
  5.���ǿ�SimpleContextValve.invoke()��������ʵ����wrapplerִ�е�
    wrapper.invoke(request,response);
    ��ע�������wrapper����Mapper����servlet name�ҵ���Ӧ��wrapper
  6.���ǿ�SimpleWrapper.invike()��ʵ�֣�
    pipeline.invoke(request,response);  Ҳ����pipeline��ִ�е�
  7.��ô�����ҵ�SimpleWrapper��basic pipele:
    Servlet servlet = wrapper.allocate();
    ����SimpleWrapper.allocate()����ִ��servlet����ص�
  8.���ǿ�SimpleWrapper.allocate()������ʵ�֣�
    ���õ���SimpleWrapper.loadServlet()������loadServlet()�����У�����servletName��ȡservletʵ���ĺ��Ĵ����������У�
    Loader loader = getLoader();
    ClassLoader classLoader = loader.getClassLoader();
    myClass =    classLoader.loadClass(servletName);

    8.1 getLoader()��ȡLoader��
      ���ǿ�SimpleWrapper.getLoader()������ȡLoader�Ĵ��룺
      parent.getLoader();  �ɼ���SimpleWrapper�Ǵ�����parent����ȡ��Loader��
      Ҳ���Ǵ�SimpleContext����ȡLoader��֮ǰ������Bootstrap���Ѿ�������Loader��StandardContext��WebappLoader
      Ҳ����˵��������SimpleWrapper��getLoader()�õ���Loader��WebappLoaderʵ��
    8.2 ����Loader��ȡClassLoader
      ���ǿ�������������servletʵ���Ĳ���Loader������ClassLoader����ôCLassLoader����ô������
      ����Ĵ���ǳ�������Ǹ���loader.getClassLoader()��ȡ����
      ��ô���ǿ�����ʵ��,���Կ���WebappLoader.getLoader()��������ֻ��һ��getter()������return this.classLoader;
      ������classLoader����WebappLoader.start()��ʱ�򴴽���(��Ҫ�������)
    8.3 ��ClassLoader����servletʵ��

  11.WebappLoader.start()����Щ�����أ���ʵ���ǵ���WebappLoader.createClassLoader()
     ����һ��WebappClassLoaderʵ��
  12.���գ����WebappClassLoader.loadClass(ServletName)������ʵ��
    myClass = classLoader.loadClass(servletName);
    �����ԣ���ͨ������WebappLoader.loadClass
    Ϊ�˾����ͨLoader���ܣ����ǽ�������ȸ���WebappClassLoader��ʹ֮�ܹ�ʵ��loadClass(servletName)�Ĺ���
  13.��Ҫ�����һ�����飬����Loader.start()����˵WebappLoader.start()��˭�����ģ�
     ����ֻҪ��һ��chap6/SimpleContext.start()��֪���ˣ�Context.start()��ʱ�򣬻�����context��ϵ�е�ʵ��������Loader/Wrapper�ȶ���ʼ��һ�顣
     ��Ȼ����������ʹ��StandardContext������ԭ����һ���ġ�

20200714
����һ�����ڣ�������ϰһ��Loader������Tomcat�е�λ�á�

20200718
ǰ��˵����ô�࣬�ܽ�һ�£�����Ҫ��֤Loader�Ļ������ܣ������Ȳ���ԭ����SimpleClassLoader�ķ�ʽ
����������μ����



������
���������Ϊ�ܾö�û����tomcat�ˣ��������ǻ��˺ܶ�ʱ�䣬�ع�tomcat������ܹ����ж෽���ԭ��
һ����ԭ����tomcat����ṹȷʵ�ܸ��ӣ������������Ҫһֱ����ѧϰtomcat����֤����ÿ����ѧϰһ�£�������tomcat�ܹ�ʱ�̼�������
ͬʱ��Ҫ��һ������ͼ���������ͼ��ʲô�����أ�Ҫ����tcmcat���յ�һ���ͻ���servlet http���󣬵�������servlet�ദ��ͻ����������������

һ���������Ǵ�windowsǨ�Ƶ�Mac֮��û�к��ʵ�����visio�Ļ�ͼ���ߣ��������һֱ���Һ��ʵĹ��ߣ�
һ����������Ŀǰ��package�ṹ������Ŀǰ�Ľṹ�ǣ�ÿ�»�����������Ĺ��ܷŵ���ǰ��package�£�֮ǰ�½����Ѿ������õ����ݣ��Ḵ��
�����ļܹ������������ǣ��ڶ�λ֮ǰ�����ʱ�򣬿��ܻ�ȥ�ü����½��ҡ�֮ǰ���½ڿ��ܻ��ж��ʵ�֣�����Context��Wrapper�ȵ�
֮ǰ�ܶ��½ڶ��С��������ǵ��Ծͷǳ��鷳��

Ϊ�˽��������⣬���ǿ�����Ҫ����һ��Ŀǰ�ļܹ���
�ѵ�ǰ����ÿ�������ļܹ�������Ϊͳһ��һ�����̡������������Ĺ��ܣ����������������ӡ�
����Ҿ��ÿ�����Ϊ�����γ�ѧϰ��ɺ��һ���κ���ҵ���Ѹ����½ڵ����ݸ�ϰһ�飬Ȼ��Ѹ����½ڵĹ��ܶ��ŵ�һ������Ĺ�����ȥ��

�������ڰ�Servlet��ͨ�ˣ���
��Ҳ��ζ���Ѿ��ָ�״̬�ˣ���
��20200625��20200714�����˴����µ�ʱ��Żָ�״̬��̫�������ˣ�����
����Ҫ�ر����ӣ��־ñ���״̬��Ѫ�Ľ�ѵ��

20200719
��ȻSimpleLoader�����Ѿ�����ͨ�ˣ��Ϳ��Կ�ʼ����Loader���¹�����
WebappLoader.setRepositories() ���Գɹ�
�����������createClassLoader()����������WebappClassLoaderʵ����
�ܹ�ͨ��setRepositories()��������WEB-INF/classes��WEB-INF/lib
��֮ǰdcreateSimpleClassLoader()���ܷḻ�˺ܶ�

Ϊ����֤���ǵĳɹ������԰�ԭ����webrootĿ¼�µ�servlet��ɾ����
cd /Users/zhoushuo/Documents/workspace/TomcatWin/webroot
mv ModernServlet.class ModernServlet.class_bak
mv PrimitiveServlet.class PrimitiveServlet.class_bak

���������Ϳ��Կ�ʼ�о�reload��

20200726
���쿪ʼ�о�reload
�����reload����㶨�ˣ��ܾóɾ͸У�
��������һ�����⣬�������ǿ�����tomcat�����ص�servletò���е����⣬�����Ǵ�WEB-INF/classesĿ¼��ȡ��

20200730
���������λclass������⣬��ô����أ�
���ûɶ��˵�ģ����ǵ���������
��������Ӧ���ҵ��ˣ���ʵ��ֻҪ��һ�����Ǽ��ص�servlet�����·�������ˣ�
��־��
class resource of the servlet instance: file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/

��ش��룺
SimpleWrapper.loadServlet()

���������������Ѿã�֮ǰ������SimpleLoader����Servlet��ʱ��������Ѿ����ˡ�
�����Դ���Ƿ���Ļ���������ʵ��
���Ǿ͵����ͷ�����飬��ǿһ�£�
��һ����������֤һ��class�ļ��ļ���


20200801
������Ϊ��ClassLoaderTest.java
������������jar���е�class�ļ�����ͨ�ļ�·���µ�class�ļ����ɹ���

���������Գ���ͨ��һ�����Գ���ģ��
WebappLoader.createClassLoader()����WebappClassLoaderʵ���Ĵ��룬����ʵ�ʵ�LoaderЧ�����

Ŀǰ���ǵ������ǣ�
���Ǽ��ص�servlet Class ��getResource()�Ľ��Ϊ��
file:/Users/zhoushuo/Documents/workspace/TomcatWin/out/production/TomcatWin/
ִ�н��Ҳ�Ǻ����getResource()���һ��������ִ�����outĿ¼�µ�class��:ModernServlet.class

����Ȼ�����ǵ�Ԥ�ڲ���
���ǵ�Ԥ���ǣ�
���Ǽ��ص�servlet Class ��getResource()�Ľ��Ϊ��
file:/Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes/
Ҫִ�����Ŀ¼�µ�class��:ModernServlet.class


�������������ˣ�
ͬʱ��Ҳ�����reload���Ե����⡣
���Ŀ��Ŀ��ģ�
��Ȼ֮ǰ����tomcat�Ĺ��̺�ʹ�࣬������������֮�󣬻��Ǻ����ջ�ģ�
���������Loader����Ľ����ѧϰ��java load��ԭ���ǳ����ģ�


���Ⱥú��ܽ�һ�¡�


