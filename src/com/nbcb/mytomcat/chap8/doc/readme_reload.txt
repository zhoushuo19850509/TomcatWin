
����ĵ���Ҫ����������reload�������

================================reload��������
1.����һ���첽�߳�
WebappLoader�������һ���첽�̣߳�����߳�����������ο����룺
WebappLoader.run()

2.ѭ��У��servlet class�Ƿ��޸�
һ��whileѭ��������WebappClassLoader��modified()�����������servlet class�Ƿ��޸�
WebappClassLoader.modified()�����ľ���ʵ����ο���
org.apache.catalina.loader.WebappClassLoader.modified()

��ʵҲ�Ƚϼ򵥣����ǵ���ʼ����ʱ�򣬰�tomcat servlet����·���µ�class�����Ϣ�ռ�������
����һ����Ϣ����lastModifiedDate����������޸ĵ�����ʱ�䣬Ȼ��Ѹ���class���lastModifiedDate��Ϣ�ŵ�һ�������У�
lastModifiedDates

����modified()�����ͻ����tomcat servlet����·���µ�class�࣬�����µ�lastModifiedDate��lastModifiedDates�����ж�Ӧ����ʱ����бȶԡ�
һ������lastModifiedDate��һ��(˵���ļ��и���)�������Ϸ���true


3.һ������servlet class�����޸ģ������첽�߳�ȥ����
����֪ͨ��Ӧ��context reload
����WebappLoader.notifyContext()
��������ܼ򵥣���������һ���������̣߳�ȥ����
����߳����������һ���ڲ��ࣺ WebappContextNotifier

��Ҫ�ر�ע�⣬�ڵ���notifyContext()�첽�߳�֪ͨcontext reloadd֮����Ҫ��������whileѭ����
���ⷴ��reload

4.context reload
�����reload�߼���ο�:
org.apache.catalina.core.StandardContext.java / StandardContext.reload()
���ǿ�һ��reload()������ʵ�֣�
������˵��������һ��context
��ϸ���������Ȱѹҿ���context�µ�wrapper ��stop
Ȼ���ٰ���Щwrapper start
�����߼�Ҫ�ٿ���

5.����Loader
����֪��,��context.reload()�����У��ᴥ��Loader������������
Loader������������ʵ���ǵ���WebappLoader.stop()/start()����
���ԣ�������reload������ʵ����WebappLoader.stop()/start()��������ɵġ�


================================reload�ļ�������
����1 ��Ȼreload()�Ķ�������WebappLoader����첽�߳�����ɵġ�
��ô��WebappLoader����첽��������ô�������أ�
�����������£�
1.WebappLoader.start()�л����threadStart()����������WebappLoader������첽�߳�
2.һ����⵽modified��WebappLoader���߳̾��˳���
3.������StandardContext.reload()�У���ͨ������WebappLoader.start()����������WebappLoader�첽�߳�

����2 ����servlet class�࣬��Щ�����ļ�������web.xml�ļ���jsp�ļ���js�ļ����᲻��ʵʱreload�أ�


����3 ��reload�����У��Ƿ��Ӱ��tomcat�����������
�����Ӱ�죬tomcat����ô�����ģ�

================================reload���ܷ�˼�ܽ�
1.��reload���������ȼ��صĳ���
reload����������ʵ�ʹ�����Ӧ�÷ǳ��㷺��
��������Ӱ��ƽ̨��Ϊ�˹ܿغ�̨������Ҫ���ƶ���ĳ����̨����ķ��ʡ�
��ô���أ�һ�ּ򵥵�˼·���������ݿ��еǼ�һ�ź�̨���������������Ҫ���Ʒ��ʵĺ�̨����Ǽǵ����ű��С�
�ͻ��˵��ú�̨�����ʱ�򣬺�̨��������ݿ�鿴һ�£���ǰ�����Ƿ��ں������С�
���������һ��ʲô�����أ����ǿͻ���ÿ�ε��ú�̨�����ʱ�򣬺�̨��Ҫ����һ�����ݿ⡣�����ݿ���ɽϴ��ѹ����
�Ľ��ķ���һ�����������ݿ⻺�桢��һ�������ǰ����ݻ��浽redis��
�����������Ҿ��ö��������ţ���Ϊ����ÿ�������ݿ��޸ĺ��������ݺ���Ҫˢ�»��档

һ�����ŵķ������ǰѺ�������Ϊ������Ϣ��Ȼ�����reload�ķ�ʽ�����첽�߳�ȥ��ʱ��ȡ���µĺ�������Ϣ��

��Ȼ�����ϻ��������ȼ��صķ���(����ο�ӡ��ʼǣ����������ã�������������ô������)�������Ҿ��������ʽʵ���ǱȽ�ֱ�ӵġ�
ֻҪ��һ���첽�̣߳����ܹ�ʵ�������ȼ��ء�

================================reload����ͼ
��һ����������û�һ������ͼ