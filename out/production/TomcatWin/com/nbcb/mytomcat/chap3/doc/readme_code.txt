������


1.��� BootStrap.java

2.HttpConnector.java
�������Կͻ��˵�socket����
�����߳�,����ServerSocket���󣬼������Կͻ��˵�socket����


3.HttpProcessor�������Կͻ��˵�socket����
HttpConnectorһ�����յ����Կͻ��˵�socket���󣬾Ͱ����socket����
HttpProcessor.process()�������д���

HttpProcessor�е�ҵ���߼��ࣺܶ
parseRequest()�������ͻ�������(��һ��) : GET /servlet/PrimitiveServlet?name=zhoushuo&company=nbcb HTTP/1.1
parseHeaders()�������ͻ��������http header
�����ݿͻ�������URL�ж���servlet������staticҳ�����󣬵�����Ӧ�Ĵ�����




4.
SocketInputStream.java
��ΪSocketInputStream����ͻ���socket��������(inputstream)������������߼���ḻ��
������
parseWholeRequest() // ��ȡ�ͻ�����������屨��
parseRequest()   // ��ȡ�ͻ���Http�����һ��
readHeader()     // ��ȡ�ͻ���Http�����Http headers


5.
HttpRequest.java
HttpResponse.java

������˵�����������ǳɶԳ��ֵġ�
����HttpRequest�����˺ܶ����ݣ���Ҫ��������HttpProcessor�У����ݿͻ���socket��ȡ�ĸ�����Ϣ
����servletName/staticURL,http headers,cookies�ȵ�
���������ִ�б���servletʵ�������HttpRequest����ȥ����servlet������Щ�������������ݡ�

��һ��request/response�͵�һ��/�ڶ��µ�request/response������ͬ
֮ǰ����ʵ��ServletRequest/ServletResponse�ӿ�
������ʵ��HttpRequest/HttpResponse�ӿ�

������ʲô��ͬ�أ�
���ǿ�һ��HttpServletRequest��Ķ����֪���ˣ�
HttpServletRequest extends ServletRequest
HttpServletRequest�Ǽ̳���ServletRequest������Ȼ��
HttpServletRequest�Ĺ��ܱ�ServletRequest���ӷḻ

������������������ʵ�ֵģ�
��Ӧ�ò�(HTTP)�������˺ܶ����ݣ�����
parameters(������ParameterMap)
http headers()
cookies
�ȵ�

�������ǵ�Servlet��ֻҪ�̳�HttpServlet�࣬�������ܵ�
HttpServletRequest/HttpServletResponse�����и��ӷḻ������

6.HttpRequestLine
����һ��JavaBean��������ǿͻ���HTTP�����һ��

7.������
ServletProcessor.java  // ����̬��Դ�����ر���servlet�࣬���ط�ʽ��chap2һ��
StaticResourceProcessor.java // ����̬��Դ�����ر��ؾ�̬��Դ�����ط�ʽ��chap1һ��