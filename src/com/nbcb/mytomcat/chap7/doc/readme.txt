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

======================FileLogger�÷�
�÷���������log4j����΢�е㲻һ����
log4j���������ģ�
Logger log = new Logger.getLogger(this);
log.error("Exception occur here...");

�����FileLogger���÷���������
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


======================����1
�Ƚ�log4j��Catalina FileLogger����ͬ��
��Ȼ��ӹ����,log4j����רҵһ�㡣�����Ҳ���Tomcat�϶�Ҳ��������log4j������רҵ��ӡ��־���

======================����2
���������������з���һ����ӡ��־��BUG��
����ж������(Process)����־��ӡ��ͬһ���ļ��У���ô������ļ����쳣�����⡣
����֪����Tomcat֧�ֶ���̵�ģʽ����ô�ڶ�����£�FileLogger����ô����ģ�
�Ƿ�����������,ʵ����һ��FillLogger��ȫ�ֶ���Ȼ��context.setLog()�ķ�ʽ������Ӧ���ǲ���ġ�



