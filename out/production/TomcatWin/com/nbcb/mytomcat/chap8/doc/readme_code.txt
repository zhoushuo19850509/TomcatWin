���Ĵ���˵��


�ص�����������Щ�������ӿڣ�����ʵ���ࣺ
Loader
Reloader

WebappLoader
WebappClassLoader

����UML��ο���
/Users/zhoushuo/Documents/workspace/TomcatWin/doc/UML/Loader.umdx


������������������һ�£���������ʵ����WebappLoader/WebappClassLoader����Ҫ����Щ����

=============================WebappLoader
��Ҫʵ�����¹��ܣ�
1.����������servlet loader�ࣺ WebappClassLoader
  �ο�WebappLoader.createClassLoader()

2.setRepositories
����Servlet���ŵ�·��������֮ǰSimpleLoader���õ�Servlet·��Ϊ��
/Users/zhoushuo/Documents/workspace/TomcatWin/webroot/

��Ȼ�������߼�Ҫ��ԭ��SimpleLoaderҪ���ӣ�����ο���
org.apache.catalina.loader.WebappLoader.setRepositories()

������˵�����setRepositoryҪ����������ַ��
(1)servlet���ŵ�·��
WEB-INF/classes

(2)servlete��Ҫ���õ�jar��·��
WEB-INF/lib



3.set class path
�����Ҫ��Ϊ������JSP�������ģ�������ʱ�Ȳ�����

4.ͨ������servlet����Ȩ�ޣ�������ȫ��

  ������ο���
  org.apache.catalina.loader.WebappLoader.setPermissions()


5.ͨ���첽�߳�ʵ��servlet reload����
  ������ο���WebappLoader.run()
  �ĵ���ο���readme_reload.txt
  ��Ȼ��reload�����̿�����ͨ��UML�ú�����һ��

=============================WebappClassLoader
��������˵��WebappClassLoaderʵ�ֵĹ��ܺ�֮ǰSimpleLoader��࣬
���Ǵ���һ��URLClassLoader����󣬹�SimpleWrapper.loadServlet()���ã�����servlet���Ƽ���servletʵ��
��Ȼ�������class loader���ܸ�Ϊǿ��

����ʵ�����¹��ܣ�
1.ʵ��servlet class load����
  ʵ�ַ�ʽ��֮ǰ��SimpleLoader���ƣ���Ҫ�ǽ���java.net.URLClassLoaderʵ�ּ���
  ���м��ص�servlet class����Ϣ�����������ResourceEntry�С�

������ز���Ϊ��
(1)
(2)
(3)
(4)
(5)

�����߼��ο����룺
org.apache.catalina.loader.WebappClassLoader.loadClass(servletName)

2.ʵ��cache������servlet����Ч��
  ����cache������servlet class����ʵ��������:
  Map<String ,ResourceEntry> resourceEntries = new HashMap<String, ResourceEntry>();

����ʵ�ֲο���
��org.apache.catalina.loader.WebappClassLoader.findResource()����
����������ȳ��Դ�resourceEntries�п�һ�µ�ǰҪ���ص�class��֮ǰ�Ƿ��Ѿ����ع�

3.���servlet����û�и���
���������Ҫ��ͨ��ʵ��Reloader.modified()����ʵ�ֵ�
����ʵ���߼��ο���org.apache.catalina.loader.WebappClassLoader.modified()����

modified()�������߼��ܽ�����:
(1)WebappLoader������ʱ���ȵ���WebappClassLoader.setLastModifieds();������
   ��repository(/WEB-INF/classes)�������ļ���lastModified�ֶ�
   �ռ��������ŵ�WebappClassLoaderһ�����ݽṹ��ȥ��
(2)����WebappLoader�����첽�̣߳�����WebappClassLoader.modified()������
   ���repository(/WEB-INF/classes)�µ��ļ���lastModified�ֶ��Ƿ����޸ģ�
(3)



4.������ȫ��
��������ĳЩ��(ĳЩpackage�µ���)������
����ο�:
org.apache.catalina.loader.WebappClassLoader.filter()
packageTriggers


=============================StandardContext
��֮ǰ��SimpleContext��ͬ�����µ�StandardContext������reload()��������Ҫ�����Loader,ʵ��servlet���ȼ���


=============================ResourceEntry
ResourceEntry����һ��ͨ��Loader���ؽ�����servlet��Դ
����servlet��class��(binary code������caching)���ϴ��޸ĵ�ʱ��(����reload)��servlet URL��ַ(��·��)


=============================�������BootStrap
���������Ȼ��BootStrap
��֮ǰ���²�ͬ��֮ǰ����Load servlet�����SimpleLoader.java ��

���£�Ҫ�ù��ܸ�Ϊǿ���Loader:
WebappLoader.java