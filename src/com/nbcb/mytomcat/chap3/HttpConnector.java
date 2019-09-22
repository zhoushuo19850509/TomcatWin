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
                 * 接收来自客户端的socket请求，交由HttpProcessor处理
                 * 当然这里的process()方法是同步的，阻塞式的
                 * 只有这次process()方法执行完成，才能执行下一个。
                 * 因此，chap3这个版本一次只能处理一个客户端请求，不能处理并发请求
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
     * 和之前HttpServer.java不同的是
     * 这里HttpConnector是新起一个线程，来处理客户端请求
     */
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

}
