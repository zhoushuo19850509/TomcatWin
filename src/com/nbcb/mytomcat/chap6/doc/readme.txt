Chapter6 Lifecycle

Lifecycle��Ҫ�ǹ���servlet���������ڡ���ɶ���أ�
�ܽ�һ�£�����������������ֹͣ����container�����ʱ���ܹ�������Ӧ�ļ����ࡣ

���ǰ�漸�£�����Ӧ�����Ƿǳ����ˡ�
����Ҫ�����Listner�Ļ��ƣ��ܹ�������ã��ڻ��ͨ����Ҫ���Ѹ����ʱ���ˡ�



==========================���µ�Ҫ��
1.�������(����Context/Wrapper/Pipleline)��������ͨ��ʵ��Lifecycle��
����start()/stop()��ʱ��ִ��һ�δ���

2.�������¼������ĸ��
����¼������ĸ����Java UI����Щ����UI�ļ����¼����ơ�
���Ƶ��÷�����C#�е��¼�����/��׿iOS�е�click�¼�����ȵȡ�

3.���µĹٷ�������,SimpleContextʵ����Lifecycle�ӿ�
�������Լ�����������SimpleWrapperҲʵ����Lifecycle�ӿڡ�

4.�������ĵĴ�����LifecycleSupport
����ļ����Լ�ʵ�ֵģ���һ��������룬���ܹ���������������¼���ԭ��
�ܹ����������֮ǰ�����Listener������(SimpleContextLifecycleListener)�����ע�ᡢ��α�������

4.��Ȼ����������ڻ���BootStrap2
��BootStrap2����Ҫ��������ࣺ����SimpleContext�ļ����ࣺSimpleContextLifecycleListener
��Ҫ��BootStrap2������SimpleContext������SimpleContext���Container�Լ�����child container�������ˡ�
SimpleContext������ʱ�򣬻�����������Ӧ���¼�������SimpleContextLifecycleListener


==========================���¼�����Ҫ�ĸ���Ҫ�������
1.Lifecycle�ӿ�
Ҫ�����������ڹ���Ķ��󣬾�Ҫʵ��Lifecycle����ӿ�

2.LifecycleListener�ӿ�
Ҫ����ĳ��container����ļ����࣬��Ҫʵ������ӿ�

3.LifecycleEvent����
�������ڹ����¼����¼��кܶ�״̬���ͣ�������Lifecycle��

==========================client url
http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb
http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb