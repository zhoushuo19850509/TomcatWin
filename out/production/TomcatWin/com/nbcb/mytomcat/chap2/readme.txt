Chap2 A Simple Servlet Container
һ���򵥵�Servlet�������ܹ��������Կͻ��˵�servlet����

=====================================================�ر�˵��
Ϊɶ˵��������Ǽ򵥵��أ�
1.
��Ϊ����ִ�е�servlet�������Լ������(PrimitiveServlet.java)
�����Ǵ�war���Զ������ġ�
2.
����ִ�е�servlet��Ҫ�����ֹ���servlet��Ӧ��class�ļ��ŵ�webrootĿ¼��
��ΪĿǰ���ǻ�������Class loader�ļ���
Ŀǰ����Ҫ���ʵ�PrimitiveServlet.java���Servlet��
ֻ���Ȱ�PrimitiveServlet.java�ŵ�src��Ŀ¼��(���ܷŵ�srcĿ¼�µ�package�����㼶�������ȡ����)
Ȼ���ֹ��������java�ļ������ѱ���õ�class�ļ��ֹ��ŵ�webrootĿ¼�¡�

=====================================================����˵��
chap2���package�µĴ��룺
PrimitiveServlet.java �����Զ����servlet
�ͻ�������Ҫ�������servlet��ִ�н����
ServletProcessor1/ServletProcessor2�ֱ��������汾��HttpServer����ͻ���servlet����Ĵ�����
ServletProcessor1����������Ҫ�ǽ����ͻ���url������Ȼ������������������ͻ���Ҫ���õ�servlet��
��������������ظ��ͻ��ˡ�

ServletProcessor2����ServletProcessor1�Ļ����ϣ��ڰ�ȫ�������˼ӹ̡�
ԭ��Ҳ��ֱ�ӣ����Ŀͻ����Լ���Servlet����ServletRequest/ServletResponseת��ΪRequest/Reponse����
Ȼ���������ŵ���Tomcat Request/Reponse�������Զ���ķ�����
���磺Request.parseUri()/Reponse.sendStaticResource()
�������Ҳ��ֱ�ӣ�����ͨ��FacadeRequest/FacadeResponse����ServletRequest/ServletResponse����


����package�µĴ��룺
Request.java
  Ϊ���ܹ�����servlet������Ҫʵ��ServletRequest interface
Response.java
  Ϊ���ܹ�����servlet��Ӧ����Ҫʵ��ServletResponse interface


