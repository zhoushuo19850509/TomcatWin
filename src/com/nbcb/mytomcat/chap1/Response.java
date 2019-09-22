package com.nbcb.mytomcat.chap1;

import com.nbcb.mytomcat.util.Constants;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {

	private Request request;
	private static final int BUFFER_SIZE = 1024;
	private OutputStream output;

	/**
	 * constructor
	 * @param out
	 */
	public Response(OutputStream out){
		this.output = out;
	}

	/**
	 * set the request to get the actual uri from client
	 * such as the static resource request: /index.html
	 * @param request
	 */
	public void setRequest(Request request){
		this.request = request;
	}
	
	/**
	 * send the static resource back to the client
	 */
	public void sendStaticResource(){

		/**
		 * the File object(static resource) ,that the client request
		 */
		File file = new File(Constants.WEB_ROOT , request.getUri());

		FileInputStream fis = null;

		byte[] bytes = new byte[BUFFER_SIZE];

		try {
			if(file.exists()){
				fis = new FileInputStream(file);
				int ch = fis.read(bytes,0,BUFFER_SIZE);
				/**
				 * 这个while循环的意思是，每次都从FileInputStream中
				 * 读取固定长度的内容(长度为BUFFER_SIZE)
				 */
				while(ch != -1){
					output.write(bytes,0,BUFFER_SIZE);
					ch = fis.read(bytes,0,BUFFER_SIZE);
				}
			}else{
				String errorMsg = "HTTP/1.1 404 File Not Found" +
						"<h1>error</h1>";
				output.write(errorMsg.getBytes());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**
		 * send the file content back to the client as OutputStream
		 */


	}


	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Implementation of the ServletResponse interface
	 * @return
	 */
	@Override
	public String getCharacterEncoding() {
		return null;
	}


	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}

	PrintWriter writer;

	@Override
	public PrintWriter getWriter() throws IOException {
		writer = new PrintWriter(output,true);
		return writer;
	}


	@Override
	public void setContentLength(int i) {

	}

	@Override
	public void setContentType(String s) {

	}

	@Override
	public void setBufferSize(int i) {

	}

	@Override
	public int getBufferSize() {
		return 0;
	}

	@Override
	public void flushBuffer() throws IOException {

	}

	@Override
	public void resetBuffer() {

	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@Override
	public void reset() {

	}

	@Override
	public void setLocale(Locale locale) {

	}

	@Override
	public Locale getLocale() {
		return null;
	}
}
