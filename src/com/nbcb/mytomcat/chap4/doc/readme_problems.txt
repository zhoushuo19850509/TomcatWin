
����ĵ���Ҫ����ʵ�����µ�ʱ��������һЩ����

������������Ҫ�����ǣ�
1.һ����ʵ���˶���ӿڣ���Щ�ӿ��еķ�����ͳһ
2.

��ϸ��ο����£�

===============================����1 HttpResponseImpl������ʱ�򱨴�
����������
���Ǵ���HttpResponseImpl��ʱ�򣬱���һ����

�����������
public List<String> getHeaders(String s) {
        return null;
}

���������
Ϊɶ�����أ�
��Ϊ���ǿ���ṹͼ��
HttpResponseImpl�̳���org.apache.catalina.connector.HttpResponseBase
��HttpResponseBase��ʵ����org.apache.catalina.HttpResponse�ӿڣ�
��ʵ����javax.servlet.http.HttpServletResponse�ӿڡ�
�Ƚϸ�Ц���ǣ���������Ϊ�汾�Ĺ�ϵ��

HttpResponse �ӿ������·�����
public String[] getHeaderNames();

HttpServletResponse �ӿ������·�����
Collection<String> getHeaderNames();

���ǿ���������������������һ�������Ƿ���ֵ��һ��
�����ͻᱨ��ͻ
����Ժ�д�����ʱ��Ҫ�ر�ע�⡣
��Ȼһ��Implement����Լ��ɶ���ӿڣ�����Ҫ�ر�ע����Щ�ӿڲ�������ͬ���Ƶķ���
һ����ͬ�Ľӿ�����ͬ���Ƶķ���������ֵ����һ������Ȼ�������Բ�һ����

������������
�����ʽҲ�ܼ򵥣����ǽ������ӿ�ͬ�������ķ������͸�Ϊһ�£�
HttpResponse�ӿڷ�����
public String[] getHeaderNames();
-------->��Ϊ��
public List<String> getHeaderNames();

�Ժ����Ƶ�����Ҳ������ͬ���ķ�����
����ϰ��tomcat Java���벻���ã����޸ĳ��°汾��java����

===============================����2 HttpRequestBase.java����
�������ݣ�������˼����HttpRequest.javaʵ����ServletRequest�ӿڣ�
����û��ʵ��ServletRequest.isAsyncSupported()�Ľӿڷ�����

public class HttpRequestBase
    extends RequestBase
    implements HttpRequest, HttpServletRequest {...}


���������
Ϊɶ�������������أ�
���ǿ���HttpRequestBase��ʵ����HttpRequest�ӿ�(org.apache.catalina.HttpRequest)
��ʵ����HttpServletRequest�ӿ�(javax.servlet.http.HttpServletRequest)

Ϊɶ�ᱨHttpRequestBaseû��ʵ��HttpServletRequest�ӿڵĲ��ַ����أ�
��Ϊ���Ǽ��ɵ����̵�servlet jar���ǱȽ��µġ�
����servlet��׼���ݽ���servlet�ӿڿ϶��ǲ���������ġ�
���servlet�ӿڲ������µĽӿڷ�����Ҫʵ�֡�

��������ʽ1��
�򵥵Ľ����ʽ����Ȼ��HttpRequestBase��������һ��HttpServletRequest��ʵ�ַ���(��Ȼ�����ǿշ�����������ʵ��)

��������ʽ2��
��һ���Ƚ��ϵ�servlet jar��

�����ܽ᣺
���ǿ����Ƚ��ϵ�tomcat������tomcat4�������µ�servlet��׼�淶�ǲ����ݵġ�
����������ʵ���з���Ҫ�ر�ע�⡣��ʱ��Ļ�������ж�һ��servlet�Ľӿڹ淶��




===============================����3 ���̱��뱨��
����֪�����Դ�chap4��ʼ�����Ǿ�Ҫʵ��tomcat catalina��һЩ�ӿ�
������������org.apache.catalina֮����һЩ���뱨��
ԭ����ʵ�ܼ򵥣���ʵ�������ǵ�JDK����/catalina�����/servlet�汾�ǲ�һ�µģ�

1.����org.apache.catalina�İ���tomcat4�汾��:
/home/zhoushuo/Documents/testGit/HowTomcatWorks/how-tomcat-works-util/src/main/java/org/apache/catalina
����汾��HowTomcatWorks���������ߵĴ���ʵ��github
2.�������õ�servlet���Ǵ�tomcat8 libĿ¼��������
(/home/zhoushuo/Downloads/apache-tomcat-8.5.23/lib/servlet-api.jar)
3.���ǵ�JDK��IDEA�е�JDK8

���������
��ǰ������������Ҫ��Ҫ���ף�
1.JDK�汾

2.tomcat��ؽӿ�
org.apache.catalina

3.servlet���
javax.servlet

����Ҫ�������ÿ��Tomcat�汾��Ӧ��JDK��servlet�İ汾�ֱ���ɶ��
����ο�doc/readme_tomcat_version.txt

��ô����ֱ����Tomcat8��һ�׿�����
��Ȼ���У�Ϊɶ�أ���ΪTomcat8��һ�״�����鱾�е�Tomcat4������ȣ����ʵ����̫���ˡ�
�����鱾��Ҫ�õ�HttpRequestBase.java Tomcat8�о�û��������롣
ѧϰ�鱾�ܲ�����

��ôֱ����Tomcat4��������
servlet��catalina�����ֳɵġ������������JDK1.4�����Ѿ������ˡ�
�ܶ�JAVA�﷨����������Ҳ��һ���ˡ�

��ô����أ�
�Ҿ��ñȽϺõ���������tomcat4����catalina/servlet��Ȼ��JDK�ý�Ϊ�µģ�����JDK1.6
һ���н��ϵ�JAVA�﷨��JDK1.6��֧�֣����޸�һ��tomcat4�Ĵ��롣
��Ϊ���ǿ������汾��tomcat������ҪJDK1.X�𣬶�JDKһ�������¼��ݵġ�
һ���з�����Ϊ��Java�﷨���⣬һ�����ͨ���޸ĳ��µ�JAVA�﷨�����

���ԣ����յĽ�������ǣ�
����HowTomcatWorks������Դ�룬

1.��libĿ¼�µ�jar�������ý���(����servlet.jar)
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks/lib/*

2.��Դ��Ŀ¼�µ�catalinaԴ�����ý���
/home/zhoushuo/Documents/Tech/tomcat/HowTomcatWorks/src/org/apache/catalina/

����ٸ��ݱ���������һ�¡�
��Ҫ���������ڵ�Catalina��ʹ��Enumeration����ࡣ
�����ò�ƺ����߰汾��Java��֧���ˣ����Ա���Ŀǰ��ȡ�İ취��ע�ʹ��롣

===============================����4
����chap4/BootStrap��ʱ��ͱ���

Exception in thread "Thread-3" java.lang.IllegalMonitorStateException

Exception in thread "Thread-1" Exception in thread "Thread-2" java.lang.IllegalMonitorStateException
	at java.lang.Object.wait(Native Method)
	at java.lang.Object.wait(Object.java:502)
	at com.nbcb.mytomcat.chap4.HttpProcessor.await(HttpProcessor.java:183)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:141)
	at java.lang.Thread.run(Thread.java:748)

����ٶ�һ���֪��������ΪHttpprocessor.await()����û�м�synchronized�ؼ���

===============================����5
����httpclient���ñ���chap4/BootStrap�����ķ���:
String url = "http://127.0.0.1:8080/servlet/ModernServlet?name=zhoushuo&company=nbcb";

���ֱ���
Caused by: org.apache.http.ProtocolException:
The server failed to respond with a valid HTTP response
������������������������������

�Ӵ����ջ������Ӧ���Ƿ��ر��ĵ�http header�����⡣
����������ı��ıȶ�һ�¡�����ʲôhttp header û�д�

Ϊ�˶�λ���⣬���������ص�nginx���񣬷�����̬��Դ��
http://127.0.0.1/
������������������ʻ���httpclient���ʶ�û����

��һ�����������nginx�����http header
Connection:keep-alive
Data:Sat, 14 Sep 2019 09:13:49 GMT
Etag:"59746037-264"
Last-modified:Sun, 23 Jul 2017 08:37:11 GMT
Server:nginx/1.13.3

���ǳ�����MyTomcat������response http header
��Ҫ����HttpProcessor.process()�������response httpheader :

((HttpServletResponse)response).setHeader("Connection","keep-alive");
((HttpServletResponse)response).setHeader("Server","MyTomcat-chap4");
((HttpServletResponse)response).setHeader("Date", FastHttpDateFormat.getCurrentDate());
ͬʱ������������
response.finishResponse();
���ڷ���response httpheader��
���������ʵ�ʵ��÷��ؽ��Ϊ��

<html>
...
</html>
HTTP/1.1
 200 OK
Content-Type: text/html
Server: MyTomcat-chap4
Connection: keep-alive
Date: Sat, 14 Sep 2019 10:21:05 GMT

������ֵ��ǣ��ͻ�����������ǽ��ղ���response http headers��httpclient���Ǳ���

��ô���أ�
�����ƻ�ͨ��plain Java networking�ķ�ʽ�����������򷵻صĺ�Nginx���ص����ݾ�����ʲô��һ��
plain Java networking�ķ�ʽҲû��������ʲô���⡣
ûɶ�á�

������������ackRequest(output)�����������ȷ���200״̬���᲻���һ��

��������ackRequest()��������Ȼ��Ч����
����ͨ����������ʣ��ܹ�ȡ��http header�ˣ�httpclient����Ҳ���ᱨ��
���Ƿ���˱���
getWriter() has already been called for this response
�����������ΪHttpServletResponse����getWrite()������������ģ�����ԭ��û�����о�

�����ʽ�ǰ�֮ǰע�͵�Response.getWriter()�����е����ݸĻ��������ˡ�

������������������ˣ�������֮�����µ����⣺
chap4/BootStrap.java�����󣬵�һ�ε���û���⣬���ǵڶ��ε��ñ���
org.apache.http.NoHttpResponseException: 127.0.0.1:8080 failed to respond
Ӧ���Ƿ����û����Ӧ

===============================����6




