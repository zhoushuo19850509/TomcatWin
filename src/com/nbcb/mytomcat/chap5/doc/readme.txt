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

==================���´���˵��

BootStrap1.java  // ��������ʾ��1 ��ʾ��Wrapper(Servlet)/Pipeline/vavle/��ʹ�÷���

SimpleWrapper    //an Implementation of Wrapper Interface
SimpleContext    // Context���Լ��ض��(��)Wrapper

SimplePipeline   //an Implementation of pipeline
SimpleWrapperValve  //
SimpleLoader     // ���ر���Servletʵ��
SimpleContextMapper  // Mapperʲô��˼���ɣ���������֮ǰ������web.xml�е�url��servlet���ƺ����Ǳ��ع���Servlet�Ķ�Ӧ��ϵ

ClientIPLoggerValve // valve that print the client's ip
HeaderLoggerValve   // valve that print the client's http header


BootStrap2.java  // ��������ʾ��2 ��ʾ��һ��Context��δ�����Servlet(Wrapper)














