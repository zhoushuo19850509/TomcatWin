Logger

Logger����Ӧ����ĿǰΪֹ��򵥵�һ���ˡ�
һ��������ΪLoggerģ�鱾���ͺܼ򵥡�
��һ��������Ϊ�����ڹ������õ�Logger�Ƚ϶࣬�Ƚ���Ϥ��

=====================Logger��ϵ
Logger��ϵ�Ƚϼ򵥡�
���Ⱦ���Logger�ӿ�
Ȼ�����LoggerBase�������ʵ��Logger�ӿ�
�����LoggerBase�µ��������ࣺSystemErrLogger/SystemOutLogger/FileLogger
����FileLogger��Ϊ���á�

=====================Logger��ϵ
Logger��ϵ�ĺ��ĵ�Ȼ��FileLogger������Ϊ�Ƚϼ򵥣�����û����д�������
ֱ�Ӿ͵���catalina���Դ���FileLogger ������
����������ϸ����FileLogger ���룬������־��ӡ��ԭ��
��ʱ��Ҫ��дһ��FileLogger

======================FileLogger�÷�
�÷���������log4j����΢�е㲻һ����

FileLogger���÷���������
1.ʵ����FileLogger����
FileLogger logger = new FileLogger();

2.Ȼ������FileLogger���������
logger.setSuffix(...);
logger.setPrefix("");
...

3.Ȼ���FileLogger�������ø�ĳ����Ҫʹ��Log�Ķ���
context.setLogger(logger);

4.����ڸ���������ʹ��FileLogger�����ӡ��־
logger.log("write sth here...");


log4j���������ģ�
Logger log = new Logger.getLogger(this);
log.error("Exception occur here...");


����Ȼ��log4j���÷������Ƚ�����ʵ��Ҫ�õ���־���ܵ����У���������setLogger()/getLogger()/log()����
�⼸��������ģ�巽����log4j��������Ȼ���˴��롣������һ�´��룬����log4j����ô�����ġ�
======================����˵��
1.�������
BootStrap
��ڴ��붨����FileLogger����

2.SimpleContext
�޸���setLogger()/getLogger()������ʹ��SimpleContext�����ܹ�ʹ�õ���־����

======================����1
�Ƚ�log4j��Catalina FileLogger����ͬ��
��Ȼ��ӹ����,log4j����רҵһ�㡣�����Ҳ���Tomcat�϶�Ҳ��������log4j������רҵ��ӡ��־���

======================����2
���������������з���һ����ӡ��־��BUG��
����ж������(Process)����־��ӡ��ͬһ���ļ��У���ô������ļ����쳣�����⡣
����֪����Tomcat֧�ֶ���̵�ģʽ����ô�ڶ�����£�FileLogger����ô����ģ�
�Ƿ�����������,ʵ����һ��FillLogger��ȫ�ֶ���Ȼ��context.setLog()�ķ�ʽ������Ӧ���ǲ���ġ�



