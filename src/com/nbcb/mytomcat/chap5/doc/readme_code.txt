
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
