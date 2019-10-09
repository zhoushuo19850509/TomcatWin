package com.nbcb.mytomcat.chap4;

import com.nbcb.mytomcat.util.Constants;
import org.apache.catalina.*;
import org.apache.catalina.net.ServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

/**
 * ������˵��HttpConnector�Ĺ��ܺ͵����µĲ��
 * ��������һ��ServerSocket���󣬼������Կͻ��˵�����
 * ��Ȼ���͵���������ȣ��кܶ�Ľ���
 * 1.֧�ֶ��̵߳Ŀͻ�������(ά����һ���̳߳�)
 * 2.ʵ����tomcat��Connector�ӿ�
 *   ��Ȼ����ԭ��Connector�Ļ����ϣ�������һЩ������
 *   getContainer()/setContainer()
 *   initialize()  // ��ʼ��webcontainer�̳߳�
 *   start()  //
 * 3.������Container�ĸ��
 *
 *
 */
public class HttpConnector implements Connector,Runnable{

    /**
     * java Container: SimpleContainer
     * Container��ΪJava����������Servlet����
     */
    private Container container = null;

    /**
     * whether the server is stopped
     */
    public static boolean stopped = false;

    /**
     * whether the server is started
     */
    private boolean started = true;

    /////////////////////// �̳߳���� start ///////////////////////
    /**
     * ά��һ���̳߳أ��������Կͻ��˵�����
     * ����̳߳��еĶ��󣬾���HttpProcessor
     */
    private Stack processors = new Stack();

    /**
     * �̳߳���С�߳�����(�̳߳س�ʼֵ)
     * ����Ҫ���̳߳����÷ŵ������ļ���
     */
    protected int minProcessors = 5;

    /**
     * �̳߳�����߳�����
     */
    private int maxProcessors = 20;

    /**
     * �̳߳ص�ǰʵ�ʵ��߳�����
     * ����ÿ��new һ��HttpProcessorʵ������������������1
     */
    private int currProccessors = 0;

    /////////////////////// �̳߳���� end ///////////////////////


    /**
     * ����˼�����ServerSocket
     */
    private ServerSocket serverSocket = null;

    /**
     * ���bufferSize��Ҫ����Connector��ָ����ȡ�ͻ���socket��buffer��С
     * ��Ϊ�ͻ�������ֻ�ܷ���connector���޷�����HttpProcessor
     */
    private int bufferSize = 2048;


    /**
     * @��������˵��
     * 1.����HttpConnector�����߳�
     * 2.��ʼ���̳߳أ����ұ�֤�̳߳�����߳��������ٱ�������С����������
     */
    public void start(){

        /**
         * ������ǰ��HttpConnector�߳�
         */
        started = true;
        startThread();

        /**
         * ���̳߳ؽ��г�ʼ��
         * ֱ���̳߳���HttpProcessorʵ���������ﵽ��minProcessors         *
         */
        while(currProccessors < minProcessors){
            /**
             * ��ǰ�߳�����������߳���maxProcessors���˳�whileѭ��
             * Ϊɶ�����̳߳���С������ôά�֣�
             */
            if(maxProcessors > 0 && currProccessors >= maxProcessors){
                break;
            }

            /**
             * һ�����ֵ�ǰ�߳���С����С�߳���minProcessors
             * �ʹ����µ�HttpProcessorʵ��
             * �����̳߳�������һ���µ��߳�
             */
            HttpProcessor processor = newProcessor();
            recycle(processor);
        }
//        System.out.println("A thread is initialized ,the number of " +
//                " current Proccessor is : " + currProccessors);

    }

    /**
     * @��������˵��
     * HttpConnector�ĳ�ʼ������
     * ��ʼ����ʱ����Ҫ����Щ���飺
     * 1.����Server Socket (�ο�chap3�е�HttpConnector)
     * 2.�������õ�webcontainer��С����ʼ��һ���̳߳�(chap4��������)
     * 3.
     *
     * @���Ż�
     * 1.����ServerSocket�Ĺ��̣�����factory����(org.apache.catalina.net.ServerSocketFactory)
     *
     * @throws LifecycleException
     */
    @Override
    public void initialize() throws LifecycleException {

        /**
         * ʵ����Serversocket���������Կͻ��˵�����
         */
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port ,1 , InetAddress.getByName("127.0.0.1"));
            System.out.println("ServerSocket established! Start listening on port : " + port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /**
         * ����һ���̳߳أ����ڲ����������Կͻ��˵�����
         */
    }



    /**
     * @��������˵��
     * HttpConnector�����̷߳�����
     * 1.����һ��ServerSocket�������������Կͻ��˵�socket��������
     * 2.Ȼ���Դ��̳߳��л�ȡһ��HttpProcessor��Դ���������socket����
     */
    @Override
    public void run() {

        System.out.println("Start the thread of the default connector : " + this.getClass().getName());

        while (!stopped) {
            Socket socket = null;

            /**
             * accept the incoming connection
             */
            try {

//                System.out.println("HttpConnector accept the incoming client");
                socket = serverSocket.accept();


                /**
                 * ����socket���ӳ�ʱʱ��
                 */
                socket.setSoTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT);
                socket.setTcpNoDelay(true);



                /**
                 * Ȼ����̳߳���ȡ��һ��HttpProcessor���󣬴���ͻ�������
                 */
                HttpProcessor processor = createProcess();

                /**
                 * ��processor����Ϊnull��Ҫ��Ϊ����֤����˹ر�socket����
                 * �����쳣���ͻ��˵ĳ���
                 */
//                HttpProcessor processor = null;

                /**
                 * ÿ�η���һ��HttpProcessor�߳���Դ֮�󣬶���ӡһ�µ�ǰ�̳߳ص�״̬
                 */
                printThreadPoolInfo();

                /**
                 * ����̳߳��е���Դ�Ѿ��ľ���socket�Ͻ��ر��˰�
                 * ������Ѿ�����Ϊ����
                 */
                if(processor  == null){
                    System.out.println("���̳߳ػ�ȡ���õ�HttpProcessorʧ�ܣ�");
                    socket.close();
                }else{
                    /**
                     * ����̳߳��л��п��õ���Դ
                     * ���ϰ�socket���ɸ�������õ�HttpProcessor�����к����Ĵ���
                     */
                    processor.assign(socket);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                /**
                 * if catch an Exception
                 * we continue the Http server to listen to the next incoming connection
                 */
                continue;
            }
        }
    }

    /**
     * ������ǰHttpConnector�߳�
     * �߳��������߼���ο�run()
     */
    public void startThread(){
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void printThreadPoolInfo(){
        /**
         *  ��ǰprossors���stack�Ĵ�С
         *  Ҳ���ǵ�ǰ���õ��߳���Դ
         */
        System.out.println("current Proccessor  stack no: " + processors.size());

        /**
         *  ��ǰ�ж���HttpProcessor�̶߳��󱻴�������
         *  һ����˵��currProccessors�϶��Ǵ��ڵ���processors.size()�ģ�
         *  �������������ʵ����Ŀǰ���ڴ���ͻ���socket servlet�����
         */
        System.out.println("current HttpProccessor  no: " + currProccessors);

        /**
         * ��ӡ�����߳�ʹ�ô���
         */

    }

    /**
     * ���������Ҫ���ܾ��Ƿ���HttpProcessor����
     * ��Ȼ���󲿷�ʱ�䲻�Ǽ򵥵�newһ��HttpProcessor����
     * ����Ҫ���̳߳���ȥȡ�������ʹ�������˴���ͻ��������Ч�ʣ�
     * ����ÿ�ζ�new һ��Socket������ͻ�������
     * @return ����HttpProcessor����
     */
    public HttpProcessor createProcess(){
        HttpProcessor processor = null;

        synchronized (processors){
            /**
             * ����̳߳ز�Ϊ��
             * ֱ�Ӵ��̳߳��л�ȡһ��HttpProcessor����ʵ��
             */
            if(!processors.empty()){
                return (HttpProcessor) processors.pop();
            }

            if((maxProcessors > 0) && (currProccessors < maxProcessors)) {
                return newProcessor();
            }


//        System.out.println("fetch a thread from the thread pool : " + processor.toString() );
            return processor;

        }

    }

    /**
     * ���������������һ���µ�HttpProcessorʵ��
     * ʵ��������������������߳�
     * @return
     */
    public HttpProcessor newProcessor(){
        HttpProcessor processor = new HttpProcessor(this,currProccessors++);

        processor.start();

//        System.out.println("a new thread(HttpProcessor) is created! thread info: " + processor.hashCode() );
        return processor;
    }

    /**
     * ������������ǡ����ա�
     * ʵ�����ǽ�HttpProcessor����ŵ��̳߳���ȥ
     * @param processor
     */
    public void recycle(HttpProcessor processor){
        processors.push(processor);
    }


    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;

    }

    @Override
    public boolean getEnableLookups() {
        return false;
    }

    @Override
    public void setEnableLookups(boolean enableLookups) {

    }

    @Override
    public ServerSocketFactory getFactory() {
        return null;
    }

    @Override
    public void setFactory(ServerSocketFactory factory) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getRedirectPort() {
        return 0;
    }

    @Override
    public void setRedirectPort(int redirectPort) {

    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public void setScheme(String scheme) {

    }

    @Override
    public boolean getSecure() {
        return false;
    }

    @Override
    public void setSecure(boolean secure) {

    }

    @Override
    public Service getService() {
        return null;
    }

    @Override
    public void setService(Service service) {

    }


    /**
     * ����Request����
     * @return
     */
    @Override
    public Request createRequest() {
        HttpRequestImpl request = new HttpRequestImpl();
        request.setConnector(this);
        return request;
    }

    @Override
    public Response createResponse() {
        HttpResponseImpl response = new HttpResponseImpl();
        response.setConnector(this);
        return response;
    }


    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


}
