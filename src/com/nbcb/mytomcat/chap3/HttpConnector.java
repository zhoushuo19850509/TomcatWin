package com.nbcb.mytomcat.chap3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpConnector implements Runnable {

    private boolean stopped = false;



    @Override
    public void run() {

        ServerSocket serverSocket = null;

        int port = 8080;

        try {
            serverSocket = new ServerSocket(port ,1 , InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while(!stopped) {
            Socket socket = null;

            /**
             * accept the incoming connection
             */
            try {
                /**
                 * �������Կͻ��˵�socket���󣬽���HttpProcessor����
                 * ��Ȼ�����process()������ͬ���ģ�����ʽ��
                 * ֻ�����process()����ִ����ɣ�����ִ����һ����
                 * ��ˣ�chap3����汾һ��ֻ�ܴ���һ���ͻ������󣬲��ܴ���������
                 */
                socket = serverSocket.accept();
                HttpProcessor processor = new HttpProcessor();
                processor.proccess(socket);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                /**
                 * if catch an Exception
                 * we continue the Http server to listen to the next incoming connection
                 */
                continue;
            } finally {
                /**
                 * close all the resources finally
                 */
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * ��֮ǰHttpServer.java��ͬ����
     * ����HttpConnector������һ���̣߳�������ͻ�������
     */
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

}
