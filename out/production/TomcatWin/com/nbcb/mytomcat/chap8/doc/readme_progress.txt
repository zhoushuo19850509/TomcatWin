

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

20200802
�����loader˳��������ܽ���һ�¡�
ͬʱ��ʼ�о�cache



20200803
�����cache��˳��������һ��
��Ҫ�ο�Tomcat�ٷ���WebappClassLoader.java
��¼��UML����ͼ����

����ƻ�:
1.system(loader)��ô����?
2.securityManager��ô����?


20200808
����˼����һ��cache��ԭ��

20200809
���Ǽ���̽��cache��ԭ����Ҫ���Ķ�
WebappClassLoader.findResourceInternal()�Ĵ�������
Ϊɶ�������Ҫ����ô���أ���Ҫ������Ϊ��Java ���䲿�ֵ��������ȽϷ�ǳ��


20200902
���ǽ���һ����û�п�Դ���ˣ�������
����֮ǰ����cache������ݣ����е������ˡ�
0830��ĩͷ��
0823��ĩ����
0816��ĩ��֪���ڸ���
�������õ�������Ǹ����ı�Ǯ��
�Ȱ�cache������������ͨ��UMLͼ����һ�顣

20200929
��������������tomcat servlet cache�İ���
���ܽ�һ�¾��ǣ����û��cache��ÿ�θ��ݿͻ������󣬶�Ҫ����һ��servlet��
������ع��̿������ܷ��㣺
Class myClass = classLoader.loadClass(servletName);
servlet = (Servlet)myClass.newInstance();
���������÷����ԭ����class���ļ�ת��Ϊ����ʵ����
�������ο�SimpleWrapper.loadServlet()

���ǣ�������̵ı�����ʲô�أ����Ǵ��ļ�ϵͳ�л�ȡclass���ļ���Ȼ���class����м��ء�
������ع������������о�һ�£���ʵ���Ǻܸ��ӵģ�Ҫ����class�ļ���Ȼ����ص��ڴ����ִ�С�
��Ȼ��������̻�����tomcat����servlet��Ч�ʡ�
tomcat��Ϊjava����������servlet laod��鹦�ܣ���Ȼ����Ҫ�Ż��ġ�

��ô�Ż��أ�����servlet cache��˼·�����ǰ���Щ�������ع���servlet�࣬�ŵ��ڴ��С�
���ؿͻ��������������Ƶ�servlet������ֱ�Ӵ��ڴ����õ�servlet����ʵ���ˡ�

���˼·�ǳ�������������Ӧ�õ���Ŀʵ����ȥ��֮���������Ƶĳ������������ϡ�

��Ȼ���ܹ��������������Ǻܲ����׵ģ���λ�����ô�õ�ʱ�������load��ԭ��
һ�����Ƕ�java����֪ʶ���ղ��ι�(�����ԭ��JVM����Java class���ԭ��)
��һ���棬servlet laod�������ȷʵӦ����tomcat��������ѵĲ����ˡ�Ӧ�ú�֮ǰConnector�������ѵĲ���





ͨ���������Ĺ��̣��ջ��Ǻܴ�ģ�
1.��ΪJava�����·���ܳ��ܳ���Ҫѧϰ�����ݻ��ܶ�ܶࣻ
2.ֻ���Լ�����Ķ��������Լ��������յģ������������ٷḻ����ϸ��û���Լ���������û�õģ�
3.ѧϰԴ�롢��ϰԴ��ô�ʵ����̫���ˣ�ѧϰԴ����Ҫ�����õı������֧�֣���������ѧϰԴ���ٽ��Ա�����Ե���������
4.��ν��ѧ����˼���衢˼����ѧ�����ѧ˼����Ƿǳ���Ҫ�ģ�
��ѧϰ�˺ܶ�֪ʶ������û���Լ�����˼������ûɶӡ��ģ�
��ƽʱ˼���˺ܶ࣬��Ҫ�������ǣ���������ȥѧϰ�µ�֪ʶ��û������ʵ������Ҳ��û���õģ�


20201001
�����load cacheԭ��֮�󣬺����ƻ���
1.
һ����ο�һ��tomcat servlet load�ٷ�ʵ��Դ��(�������ϵ�4�汾�ͽ��µ�9�汾)���Լ���cache����ʵ���ˣ�
org.apache.catalina.loader.WebappClassLoader.java/findResourceInternal()

2.
��һ���棬ͨ�����԰������ȶ�һ�£�����cache��û��cache������
���Ǵ��������֪��������cache�����ܿ϶��Ȳ���cacheҪ�á��ؼ��Ǻö��ٵ����⡣
���û����cache���Ǽ���һ��servlet�����Ҫ����tomcat���ش��̣�����sevlet class�ļ���
�������cache��ֱ�Ӵ��ڴ���ȥ�þ����ˡ��ؼ���������ֳ���������
��ô���ͨ�����ǵĳ��򣬼���cache�����������˶����أ�

3.
�����Ҫ��Load������ݺú��ܽ�һ�¡�

Q:�������һ�����ʣ�Ϊɶ������ʵ��Ҫ��ô������class��binary��Ϊ���档
ֱ�Ӱ�class��ŵ�Map�в�����Ϊɶ��Ҫ��class���binary��
���Map��key/valueΪ��servlet path/��Ӧ��servlet Class��
A��ֻҪ��ϸ��Դ�����֪����Map�е�key/valueȷʵΪservlet name/Class
����ÿ�οͻ���������ͬһ��servlet�࣬����ӻ����и���servlet name��ȡ��Ӧ��Class��

20201009
�����ڼ�һֱ��˼�������ͨ��С������֤cache���������á�
�ܽ������¼��㣺
1.���
����Spring MVC��ܣ�ͨ������SpringDispatch����֤cache������

2.����lib
servlet�࣬�ܿ��ܻ���Ҫ�õ�������jar��.
�������Щjar��Ҳ��ǰ���ؽ�������ô��cache�������Ƿ��������ԣ�

3.����һ���Ƚϴ��servlet��
���servlet����ļ��Ƚϴ�

4.��������servlet
ģ���ڸ߲�����Ƶ������servlet��ĳ����£�cache������

������Щ�뷨����ʵ������ͨ��С������ģ�⡣��Ϊtomcat�ı��ʾ���loadһ��java����Ȼ��ִ�С�
û��ʲô�ر����صĶ��������Ҫ�����˽�Loadԭ����ȻҪ�˽�JVM�Ļ��ơ�
����С������Բο����package�µĳ���com.nbcb.mytomcat.test.load.LoadTest
�������ͨ���Ƚϸ��ִ�������ʵ���ķ�ʽ���ǳ���������ʾ��load cache���������ơ�
����Tomcat��˵��������������Ƿǳ����õġ�


��ʱ���룺
�Լ���Ҫ��Զ���־�η��
��������ѧϰtomcat����������ô�������Ǵ���ͻ���http����Ȼ��ִ��һ��servlet��Ȼ�󷵻ؽ����
����ֻҪ��������ѧϰtomcat���ᷢ�ֲ�����ô�򵥡�
������load���£�����Ҫ����JVM����Java class�ļ������֪ʶ��
��Ҫͨ���˽�JVM������淶�����������tomcat loadԭ������ʼ��δ���ġ�
������������һ���ط�������Ҫ���������侳����������ȥ��������ط���
���ֻ��ͨ����Ƭ������ȥ�˽⣬�Ǿ�������ʽ�ˡ�
���ԣ�ѧϰһ��֪ʶ��Ҳ��һ�������似�����棬ʵ���Ǳز����ٵġ�ֻ����������ȥ��������������ż�����

20201021
��Ȼ�������load cache�ĺô�����ô�������Ϳ�ʼ�ô���ʵ���ˡ�
��д����֮ǰ��������Ҫ�滮һ�¡�
����֪������ʵ��load cache֮ǰ�����ǵ�loadԭ����Բο�SimpleWrapper.loadServlet()����������Load�߼���
ClassLoader classLoader = loader.getClassLoader();
myClass = classLoader.loadClass(servletName);
servlet = (Servlet)myClass.newInstance();

����classLoader������WebappClassLoader.java
classLoader.loadClass(servletName)���õ���ϵͳĬ�ϵ�java.lang.ClassLoader.loadClass()

Ϊ��ʵ��load cache�����Ǽƻ������loadClass(String servletName)������������

������߼�����������Ҫʵ��class load�Ĺ��ܡ������Ĺ��ܣ�����Load˳��SecurityManage�ȸ߼����⣬
�������ӡ�


20201024
Ϊ�˵��Է��㣬�������ǽ���Թ�����tomcat������һ�±��룬���ݹ�����tomcatԴ���ڱ������С�

���죬��Ҫ�Ǵ��load cache�Ŀ��
�ڰ��½����󣬱���ú��ܽ�һ�£�������reload/load cache��������Ҫ�ú�����һ�¡�
�Ժ��õ�ʵ����Ŀ��ȥ��

���շݵĸ����㿴load��飬˼������ô����ʱ�䡣��������Ŷ����У�
���԰����������ж�����Ĳ�����������......
���ԣ�˼���Ǻ���Ҫ�ģ�˼������ԭ��Ϊʲô�أ�Ϊʲô�أ�Ϊʲô�أ�
���о�����ǰ֪ʶ��������Ҫ�����û�С�The Java Virtual Machine Specification���Ȿ�飬
����Ҳû�����class load�ĸ��


Q&A
Q:
���������load cache��صĴ��룬��һ��СС�����ʣ�
��Ȼ���ǵ�SimpleWrapper�а�servletʵ������������(�ο�SimpleWrapper.instance)
��ôΪɶ���б�Ҫ��servlet Class�໺�������أ�
�ѵ������ط����ܻ����õ���

A:����������⣬����ֻҪ��org.apache���package������loadClass�ؼ��־�����
���ǻᷢ�֣�loader.loadClass()��һ��ͨ�õķ�����������ֻ����Wrapper load servlet��ʱ����õ���
�κ���Ҫ����class path����Class�������ջ�ȡ��Ӧʵ���ĵط��������õ�loadClass()������
ͬʱ��loader�ĳ�ʼ��Ҳ�Ƕ����ģ������ʼ��loader�Ĵ�����Բο���
WebappLoader.createClassLoader()
�޷Ǿ���ͨ������ķ�ʽ����ȡWebappClassLoaderʵ����
Ȼ�����WebappClassLoader.addRepository()������һ�¶�Ӧ��repository�����ˡ�

��Ȼ��������Ǵ�ģ�������ϸ�о�һ�£��ͻᷢ�֣�servlet��ʵ����Ϊ���棬���ǲ��ɿ��ġ�
�ܼ򵥣���Ϊ��ͬ�ͻ��������servlet��servletʵ��������������Ȼ�ǲ�ͬ�ģ�
������A�����servlet���ú�B�����servlet���ã�����һģһ��(����clientIP��ͬ)
���Ժ���Ҫ�о�һ�£��ͻ������������Wrapper����Ӧ�û����٣��������³�ʼ����


Load����Ҫ���ȵ�����ɡ�
����������һ��ʱ��ú��ܽ�һ��Load����������

����Ӧ����ĿǰΪֹ��ʱ�������һ���ˣ�
20200625-20201024






