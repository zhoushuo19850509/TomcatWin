Chap5 Container


==================�鱾�Ķ�

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

������Pipeline�ĸ���
Pipeline����ˮ�ߣ�ĳ��Container�ᴥ��Pipeline
ÿ��Pipeline����һЩ������Valve


Wrapper Interface
Wrapper��Ҫ�����ڴ���һ��������Servletʵ����ִ��Servlet�еĸ�������
��Ҫ�ķ�����allocate()/loadServlet()
��Ҫ�����ڳ�ʼ��Servletʵ��


Context


==================���´���˵��

SimpleWrapper    //an Implementation of Wrapper Interface
SimpleContext    // Context���Լ��ض��(��)Wrapper

SimplePipeline   //an Implementation of pipeline
SimpleWrapperValve  //
SimpleLoader     // ���ر���Servletʵ��
SimpleContextMapper  // Mapperʲô��˼���ɣ���������֮ǰ������web.xml�е�url��servlet���ƺ����Ǳ��ع���Servlet�Ķ�Ӧ��ϵ

ClientIPLoggerValve // valve that print the client's ip
HeaderLoggerValve   // valve that print the client's http header

BootStrap1.java  // ��������ʾ��1 ��ʾ��Wrapper(Servlet)/Pipeline/vavle/��ʹ�÷���
BootStrap2.java  // ��������ʾ��2 ��ʾ��һ��Context��δ�����Servlet(Wrapper)














