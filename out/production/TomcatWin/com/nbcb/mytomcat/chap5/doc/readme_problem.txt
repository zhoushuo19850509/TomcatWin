����ĵ���Ҫ��¼chap5���Թ����е�һЩ����

1.����1
��ָ������
Exception in thread "Thread-5" java.lang.NullPointerException
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:121)
	at com.nbcb.mytomcat.chap5.SimplePipeline.invoke(SimplePipeline.java:67)
	at com.nbcb.mytomcat.chap5.SimpleWrapper.invoke(SimpleWrapper.java:196)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:288)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)

����ԭ�򣺴�������

2.����2
֮ǰ���õ�valve����Ч
��û�д�ӡClientIP��Ҳû�д�ӡHttp Header
����֮ǰ�����ִ��Valveִ����ִ���ˣ�����ò��Request������

��־��ӡ���£�
ModernServlet -- init
Valve ClientIPLoggerValve.invoke() ...
Client IP : null
--------------------------------------
Valve HeaderLoggerValve.invoke() ...
Not http request ,HeaderLoggerValve print nothing!
--------------------------------------

����ԭ��
1.HeaderLoggerValve ����ӡheaders��ԭ���Ǵ������⣬
HeaderLoggerValve.java��Ӧ���Ǵ�ӡRequest�е�header��Ϣ������д���ˣ���Responseȥȡheaders��

2.ClientIPLoggerValve����ӡ�ͻ���IP��ԭ���ǣ�֮ǰ����û�����ÿͻ���IP
ͨ���޸�HttpProcessor.java���ڴ���ͻ��������ʱ�򣬻�ȡ�˿ͻ��˵�IP

����3 ����BootStrap2.java����ʧ��

Exception in thread "main" java.lang.ClassCastException:
com.nbcb.mytomcat.chap5.SimpleContextValve cannot be cast to com.nbcb.mytomcat.chap5.SimpleWrapperValve
	at com.nbcb.mytomcat.chap5.SimplePipeline.setBasic(SimplePipeline.java:38)
	at com.nbcb.mytomcat.chap5.SimpleContext.<init>(SimpleContext.java:50)
	at com.nbcb.mytomcat.chap5.BootStrap2.main(BootStrap2.java:28)

Process finished with exit code 1

����ԭ����SimplePipeline.basicValve����������⣬
ԭ���Ƕ������SimpleWrapperValve������û�п��ǵ�SimpleContextValve
��ˣ�ֻҪ�����ͨ�õ�Valve���;�����



����4 ���������ModernServletʧ��
����ԭ��SimpleContextValve.invoke()������contextΪNULL����ΪSimpleContextValve.setContainer()����û��ʵ�֣��ǿյġ�
������SimplePipeline.setBasic()��û����basic container(SimpleContextValve)���ö�Ӧ��SimpleContextValve�ҿ����ĸ�container����

Exception in thread "Thread-5" java.lang.NullPointerException
	at com.nbcb.mytomcat.chap5.SimpleContextValve.invoke(SimpleContextValve.java:61)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:137)
	at com.nbcb.mytomcat.chap5.ClientIPLoggerValve.invoke(ClientIPLoggerValve.java:34)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.HeaderLoggerValve.invoke(HeaderLoggerValve.java:36)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.SimplePipeline.invoke(SimplePipeline.java:77)
	at com.nbcb.mytomcat.chap5.SimpleContext.invoke(SimpleContext.java:63)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:293)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)
java.net.SocketTimeoutException: Read timed out
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:170)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at java.net.SocketInputStream.read(SocketInputStream.java:127)
	at com.nbcb.mytomcat.chap4.SocketInputStream.parseWholeRequest(SocketInputStream.java:152)
	at com.nbcb.mytomcat.chap4.SocketInputStream.<init>(SocketInputStream.java:26)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:233)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)
Exception in thread "Thread-4" java.lang.StringIndexOutOfBoundsException: String index out of range: -1
	at java.lang.String.substring(String.java:1967)
	at com.nbcb.mytomcat.chap4.SocketInputStream.readRequestLine(SocketInputStream.java:67)
	at com.nbcb.mytomcat.chap4.HttpProcessor.parseRequest(HttpProcessor.java:362)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:252)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)

����5

"C:\Program Files\Java\jdk1.8.0_111\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2018.2.8\lib\idea_rt.jar=59751:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2018.2.8\bin" -Dfile.encoding=GBK -classpath "C:\Program Files\Java\jdk1.8.0_111\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\access-bridge.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_111\jre\lib\rt.jar;C:\Users\zs\����\testGit\TomcatWin\out\production\TomcatWin;C:\Users\zs\����\testGit\TomcatWin\lib\commons-beanutils.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-codec-1.11.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-collections.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-daemon.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-digester.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-logging-1.2.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-logging.jar;C:\Users\zs\����\testGit\TomcatWin\lib\commons-modeler.jar;C:\Users\zs\����\testGit\TomcatWin\lib\fluent-hc-4.5.10.jar;C:\Users\zs\����\testGit\TomcatWin\lib\httpclient-4.5.10.jar;C:\Users\zs\����\testGit\TomcatWin\lib\httpclient-cache-4.5.10.jar;C:\Users\zs\����\testGit\TomcatWin\lib\httpclient-osgi-4.5.10.jar;C:\Users\zs\����\testGit\TomcatWin\lib\httpclient-win-4.5.10.jar;C:\Users\zs\����\testGit\TomcatWin\lib\httpcore-4.4.12.jar;C:\Users\zs\����\testGit\TomcatWin\lib\httpmime-4.5.10.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jaas.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jakarta-regexp-1.2.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jcert.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jna-4.5.2.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jna-platform-4.5.2.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jnet.jar;C:\Users\zs\����\testGit\TomcatWin\lib\jsse.jar;C:\Users\zs\����\testGit\TomcatWin\lib\mx4j.jar;C:\Users\zs\����\testGit\TomcatWin\lib\naming-common.jar;C:\Users\zs\����\testGit\TomcatWin\lib\naming-factory.jar;C:\Users\zs\����\testGit\TomcatWin\lib\naming-resources.jar;C:\Users\zs\����\testGit\TomcatWin\lib\servlet.jar;C:\Users\zs\����\testGit\TomcatWin\lib\tomcat-util.jar;C:\Users\zs\����\testGit\TomcatWin\lib\xercesImpl.jar;C:\Users\zs\����\testGit\TomcatWin\lib\xmlParserAPIs.jar" com.nbcb.mytomcat.chap5.BootStrap2
ServerSocket established! Start listening on port : 8080
Start the thread of the default connector : com.nbcb.mytomcat.chap4.HttpConnector
current Proccessor  stack no: 4
current HttpProccessor  no: 5
current Proccessor  stack no: 3
current HttpProccessor  no: 5
Exception in thread "Thread-5" java.lang.ClassCastException: org.apache.catalina.connector.HttpRequestFacade cannot be cast to org.apache.catalina.HttpRequest
	at com.nbcb.mytomcat.chap5.SimpleContextMapper.map(SimpleContextMapper.java:63)
	at com.nbcb.mytomcat.chap5.SimpleContext.map(SimpleContext.java:854)
	at com.nbcb.mytomcat.chap5.SimpleContextValve.invoke(SimpleContextValve.java:64)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:137)
	at com.nbcb.mytomcat.chap5.ClientIPLoggerValve.invoke(ClientIPLoggerValve.java:34)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.HeaderLoggerValve.invoke(HeaderLoggerValve.java:36)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.SimplePipeline.invoke(SimplePipeline.java:77)
	at com.nbcb.mytomcat.chap5.SimpleContext.invoke(SimpleContext.java:63)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:293)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)
Exception in thread "Thread-4" java.lang.ClassCastException: org.apache.catalina.connector.HttpRequestFacade cannot be cast to org.apache.catalina.HttpRequest
	at com.nbcb.mytomcat.chap5.SimpleContextMapper.map(SimpleContextMapper.java:63)
	at com.nbcb.mytomcat.chap5.SimpleContext.map(SimpleContext.java:854)
	at com.nbcb.mytomcat.chap5.SimpleContextValve.invoke(SimpleContextValve.java:64)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:137)
	at com.nbcb.mytomcat.chap5.ClientIPLoggerValve.invoke(ClientIPLoggerValve.java:34)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.HeaderLoggerValve.invoke(HeaderLoggerValve.java:36)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.SimplePipeline.invoke(SimplePipeline.java:77)
	at com.nbcb.mytomcat.chap5.SimpleContext.invoke(SimpleContext.java:63)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:293)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)
Exception in thread "Thread-3" java.lang.ClassCastException: org.apache.catalina.connector.HttpRequestFacade cannot be cast to org.apache.catalina.HttpRequest
	at com.nbcb.mytomcat.chap5.SimpleContextMapper.map(SimpleContextMapper.java:63)
	at com.nbcb.mytomcat.chap5.SimpleContext.map(SimpleContext.java:854)
	at com.nbcb.mytomcat.chap5.SimpleContextValve.invoke(SimpleContextValve.java:64)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:137)
	at com.nbcb.mytomcat.chap5.ClientIPLoggerValve.invoke(ClientIPLoggerValve.java:34)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.HeaderLoggerValve.invoke(HeaderLoggerValve.java:36)
	at com.nbcb.mytomcat.chap5.SimplePipeline$SimplePipelineValveContext.invokeNext(SimplePipeline.java:132)
	at com.nbcb.mytomcat.chap5.SimplePipeline.invoke(SimplePipeline.java:77)
	at com.nbcb.mytomcat.chap5.SimpleContext.invoke(SimpleContext.java:63)
	at com.nbcb.mytomcat.chap4.HttpProcessor.process(HttpProcessor.java:293)
	at com.nbcb.mytomcat.chap4.HttpProcessor.run(HttpProcessor.java:192)
	at java.lang.Thread.run(Thread.java:745)
current Proccessor  stack no: 2
current HttpProccessor  no: 5


