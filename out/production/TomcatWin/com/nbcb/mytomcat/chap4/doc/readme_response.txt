����ͨ�����¼�������ĵ��ԣ������ܽ�һ��http response�����ݡ�
Ϊ�˳��׸����http response�����ݣ�����ϣ���ܹ�����http response raw stream
���ǣ�������ͨ��httpclient����������������������Ƿ��ֶ�û������http response raw stream
���ǿ����Ķ����Ѿ��������������ݡ�

��ô���أ�
���ȣ����ǿ������һ�£�http reqeust�Ĵ�����̡�
������֮ǰ���½��У���Դ��㼶��ʵ�ֹ���http request�Ĵ���
��ûɶ��ͷ�ģ��������д��������
��ô����http response�����Ǵ󵨼��裬Ҳ��������
��һ�飺statusline: HTTP/1.1 200 OK
�ڶ��飺content
�����飺http header

Ϊ��֤ʵ���ǵĲ��룬���ǿ�һ�·���http response���ݵ�Դ�룺

1.HttpProcessor.java
HttpProcessor.process()
���ǵ���SimpleContainer������servlet֮�󣬵������´��룺
response.finishResponse();

2.HttpResponseBase.java
HttpResponseBase.finishResponse()

������finishResponse()�п��Կ���:
sendHeaders();
super.finishResponse();

���ȵ�Ȼ�Ƿ���http headers
Ȼ����ø���ر���

sendHeaders()����չ������һ��
��ʵ˳������ȴ�ӡstatusline���ٴ�ӡhttp header

��������Ҫ��עһ��sendHeaders()�����У���ȡOutputStreamWriter�ķ�ʽ��
OutputStreamWriter osr = new OutputStreamWriter(getStream(), getCharacterEncoding());
���Կ�������ֱ����client socket outputstream������OutputStreamWriter��

3.Server������response content�Ĵ���
����������Servlet�У����servlet�����PrintWriter������ɷ�ʽ��
PrintWriter writer = response.getWriter();
writer.println("<html>");
......

���getWriter()������ο���
ResponseBase.getWriter()

ResponseStream newStream = (ResponseStream) createOutputStream();
newStream.setCommit(false);
OutputStreamWriter osr =
            new OutputStreamWriter(newStream, getCharacterEncoding());
writer = new ResponseWriter(osr, newStream);
stream = newStream;
return (writer);

��ʵ�ǲ��ģ����Ƿ�װ��һ��ResponseWriter()�����


�ۺ����ϣ����ǿ��Կ��������˳��ò���ǣ�
content->status line->http header
Ϊ��֤ʵ���ǵĲ��룬���鿴һ��http client��Դ�룬��http client����ô����server response��




