Chap5 Container


==================Chap5����ĸ���

����������Container�ĸ���
Container�൱������������һ���ӿ�
����ӿ����ĸ����ࣺ
Context/Host/Wrapper/Engine

��Ȼ���ĸ�����Ҳ�ǽӿ�

��ô�����ʵ������ʲô�أ�
StandardContext
StandardHost
StandardWrapper
StandardEngine

�����Container��ͼ��ο�visio

������Pipeline�ĸ���
Pipeline����ˮ�ߣ���˼���ǰ���һ����˳��ִ��һ����������
ĳ��Container�ᴥ��Pipeline
ÿ��Pipeline����һЩ������Valve


Wrapper Interface
Wrapper��Ϊ������һ����Ҫ�����ڴ���һ��������Servletʵ����ִ��Servlet�еĸ�������
��Ҫ�ķ�����allocate()/loadServlet()
��Ҫ�����ڳ�ʼ��Servletʵ��


Context
ContextҲ��Ϊһ���������൱��WAS�е�application���������Wrapper(Servlet)

==================˼����ΪɶҪ��������һ�׼ܹ�
ΪɶҪ������ô������أ�


==========================client url
http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb
http://127.0.0.1:8080/servlet/PrimitiveServlet?name=zhoushuo&company=nbcb













