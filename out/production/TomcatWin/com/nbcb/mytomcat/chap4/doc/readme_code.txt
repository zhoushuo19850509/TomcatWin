������Ҫ�����������£�

1.BootStrap.java
2.HttpConnector.java
3.HttpProcessor.java
4.SimpleContainer.java

����HttpProcessor.java��Ȼ�����ѿе�

==========================================
��ϸ˵��һ�¸������룺



==========================================
1.BootStrap.java
�����࣬��Ϊ�������

==========================================
2.HttpConnector.java
�Լ�ʵ�ֵ�Connector�࣬�����ʵ����catalina.Connector�ӿ�

HttpConnector.java������Ҫ�ķ����У�
getContainer()/setContainer()   // ����container�����ں��������servlet����
initialize()   // ����ServerSocket��
start()        // ����HttpConnector���̣߳�ͬʱ��ʼ��(Httpprocessor)�̳߳�
run()          // HttpConnector�����̷߳�����whileѭ������ʼ�������Կͻ��˵�socket����

���¸Ľ��ĵط��У�
֧�ֶ��߳�:ͨ��ά��һ��(HttpProcessor)�̳߳أ�ʵ���˴������ͻ�������Ĺ���


�����ȻҪ������֮ǰ�ڵ����µ�HttpConnectorҪרҵ��
������ο�Connector�ӿ��е�ע��

����Connector�Ĺ��ܣ������Ѿ��ڵ������Ѿ������Թ��ˣ���������һ��Socket������
�������Կͻ��˵�Socket����Ȼ��ѷ���˵Ĵ��������ظ��ͻ���

==========================================
3.HttpProcessor.java
Http������

�������Կͻ��˵�socket����

����֪����֮ǰHttpConnectorͨ��ά��һ��(HttpProcessor)�̳߳أ�
ʵ���˲���֧�ֿͻ���socket���ӵĹ��ܡ�
��ô��HttpProcessor�࣬��Ȼ��һ���߳��ࡣ
����֪������ν���̳߳أ�������ǰ׼��һЩ�߳����ʵ������Ҫ�õ�ʱ�����ϴ��̳߳���ȡ��һ��ʵ����
HttpProcessorҲ��һ����һ��HttpProcessorʵ���ŵ��̳߳��У�������������HttpProcessor Thread instance
HttpProcessor.run()�����У�������һ��while()ѭ����һ���пͻ���socket����������Ϳ�ʼ����
��ΪHttpProcessor���̳߳��е�ʵ������Ȼ������ͬ�������⡣�����Ҫ����synchronized����

ʵ����HttpProcessor���߳�֮�󣬺�������Щ�߼���chap3/HttpProcessor��ʵ�����Ƶģ��޷Ǿ���:
�����ͻ�������(��һ��);
�����ͻ��������http header

chap4���ӽ�һ�������˽����ͻ������󣬻�Ҫ���ݿͻ��������protocol��֧��http1.1Э��
��ô֧��HTTP1.1Э���أ�
����ͻ�������Ϊ��
GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1

"HTTP/1.1"˵���ͻ�����ͨ��HTTP/1.1Э����ʷ���˵ġ�
���Ƿ���˾���ʵ�֣�
(1)Http������
(2)chunk

==========================================
4.SimpleContainer.java
����������Container�ĸ���
SimpleContainerʵ����tomcat��Container�ӿڣ�
org.apache.catalina.Container.java

SimpleContainer������ú�chap3/ServletProcessor���ơ�
ֻ����ServletProcessor����רҵ��ʵ����Container�ӿ�

SimpleContainer.java��ؼ��ķ����ǣ�
invoke(Request request, Response response)
��Ҫ��load һ�� servlet�࣬ͬʱ�������servlet��service()������ִ��servlet��


==========================================
5.
HttpRequestImpl.java/HttpResponseImpl.java

������Request/Response���󣬼̳й�ϵ��ο���How Tomcat works���Ȿ��

HttpRequestImpl��Ҫ��ʲô�����أ����������chap3���Ѿ�ʵ�����ˣ��޷Ǿ����ܹ���ȡ
http headers/http parameters�ȵ�

HttpResponseImpl�޷Ǿ���getWriter()����ӡ������̨��


==========================================chap3
6.SocketInputStream
���ܺ�chap3���ƣ������ͻ���socket��������

7.HttpRequestLine
���ܺ�chap3���ƣ�������󱣴���ǿͻ���socket�������ݵĵ�һ��



