package com.nbcb.mytomcat.chap1;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class Request implements ServletRequest{

	private InputStream input;
	private String uri;


	/**
	 * default constructor
	 */
	public Request(){

	}

	/**
	 * constructor
	 * @param in
	 */
	public Request(InputStream in){
		this.input = in;
	}
	
	/**
	 * parse the incoming HTTP request
	 */
	public void parse(){
		/**
		 * this StringBuilder contains the url of the request
		 */
		StringBuilder request = new StringBuilder();

		/**
		 * the buffer bytes
		 * we does not put the request content to the Memory at once
		 * but deal with the content by buffer
		 */
		byte[] buffer = new byte[2048];
		int i ;

		try {
			i = input.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;  // read nothing from client
		}

		for(int j = 0 ; j < i ; j++){
			request.append((char)buffer[j]);
		}
		/**
		 * parse the uri from client
		 */
		 this.uri = parseUri(request.toString());


	}

	/**
	 * parse the request uri from client
	 * @param requestString GET /index.html HTTP/1.1
	 * @return /index.html
	 */
	public String parseUri(String requestString){
		String parsedUri = "";
		int index1 = -1;
		int index2 = -1;

		index1 = requestString.indexOf(" ");
//		System.out.println(index1);
		if(index1 != -1){
			index2 = requestString.indexOf(" ",index1 + 1);
		}
//		System.out.println(index2);
		if(index2 > index1){
			parsedUri = requestString.substring(index1 + 1,index2);

		}
		System.out.println("original url: " + requestString);
		System.out.println("parsed url: " + parsedUri);

		int index3 = -1;
		index3 = requestString.indexOf("\n");
		System.out.println("index3: " + index3);

		if(parsedUri != "" && parsedUri != null && parsedUri.length() > 0){
			return  parsedUri;
		}else{
			return null;
		}
	}

	public static void main(String[] argss){
		Request request = new Request();
		String uri = "GET /index.html HTTP/1.1";
		String result = request.parseUri(uri);
		System.out.println(result);
	}

	/**
	 * getter/setter of uri
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}


	/**
	 *Implementation of the ServletRequest interface
	 * @param s
	 * @return
	 */
	@Override
	public Object getAttribute(String s) {
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

	}

	@Override
	public int getContentLength() {
		return 0;
	}


	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getParameter(String s) {
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return null;
	}

	@Override
	public String[] getParameterValues(String s) {
		return new String[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return null;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public String getScheme() {
		return null;
	}

	@Override
	public String getServerName() {
		return null;
	}

	@Override
	public int getServerPort() {
		return 0;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public void setAttribute(String s, Object o) {

	}

	@Override
	public void removeAttribute(String s) {

	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return null;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String s) {
		return null;
	}

	@Override
	public String getRealPath(String s) {
		return null;
	}

}
