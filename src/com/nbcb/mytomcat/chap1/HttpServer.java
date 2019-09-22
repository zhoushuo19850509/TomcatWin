package com.nbcb.mytomcat.chap1;

import com.nbcb.mytomcat.chap2.ServletProcessor1;

import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author zs
 *
 */
public class HttpServer {
	
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";


	/**
	 * the tag of whether the app server is shutdown
	 */
	private boolean shutdown = false;
	
	/**
	 * this function establish the SocketServer 
	 * listen the incoming http request
	 */
	public void await(){
		ServerSocket server = null;
		
		int port = 8080;
		
		try {
			server = new ServerSocket(port ,1 ,InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(!shutdown){
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			
			/**
			 * accept the incoming connection
			 */
			try {
				socket = server.accept();
				
				input = socket.getInputStream();
				output = socket.getOutputStream();
				
				/**
				 * create the Request object
				 * and parse the incoming request
				 *
				 */
				Request request = new Request(input);
				request.parse();
				
				/**
				 * create the Response object
				 */
				Response response = new Response(output);
				response.setRequest(request);

				if(request.getUri().contains("servlet")){
					/**
					 * if the request url contains the important key "servlet"
					 * it means ,the client is accessing the servlet
					 */
					ServletProcessor1 processor1 = new ServletProcessor1();
					processor1.process(request,response);

				}else{
					/**
					 * else , the client is requesting static resource
					 */
					response.sendStaticResource();
				}

				
				/**
				 * check whether the client want to shutdown the connection
				 */
				shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				/**
				 * if catch an Exception 
				 * we continue the Http server to listen to the next incoming connection
				 */
				continue;
			}finally{
				/**
				 * close all the resources finally
				 */
				try {
					input.close();
					output.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
		}
		
		
	}
	
	public static void main(String[] args){
		HttpServer server = new HttpServer();
		server.await();
		
		
	}

}
