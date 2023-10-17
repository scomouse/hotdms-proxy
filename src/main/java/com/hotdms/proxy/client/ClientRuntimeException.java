package com.hotdms.proxy.client;

import com.hotdms.proxy.web.WebUtils;

/**
 * 代理IP被有道封杀出现的异常
 * @author lemon
 *
 */
public class ClientRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -1958850977435815227L;
	
	public static final int TYPE_HOST_CONNECT = 1;
	
	public static final int TYPE_HOST_RESPONSE = 2;
	
	public static final int TYPE_HOST_SSL = 3;
	
	public static final int TYPE_HOST_DNS = 4;
	
	public static final int TYPE_SOCKET_READ =  5;
	
	public static final int TYPE_SOCKET_TIMEOUT = 6;
	
	public static final int TYPE_CONNECT_TIMEOUT = 7;
	
	private int errorType;
	
	private long requestTime;
	
	private long errorTime;

	public ClientRuntimeException(int errorType, long requestTime, String message, Throwable errorStack) {
		super(message, errorStack);
		this.errorType = errorType;
		this.requestTime = requestTime;
		this.errorTime = System.currentTimeMillis();
	}

	public ClientRuntimeException(int errorType, long requestTime, String message) {
		super(message);
		this.errorType = errorType;
		this.requestTime = requestTime;
		this.errorTime = System.currentTimeMillis();
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

	public boolean isTimeout() {
		return errorType == TYPE_SOCKET_TIMEOUT || errorType == TYPE_CONNECT_TIMEOUT;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public long getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(long errorTime) {
		this.errorTime = errorTime;
	}
	  
	public long getTimeout() {
		return errorTime - requestTime;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + "[ After " +  ( errorTime - requestTime ) +" millseconds At " + WebUtils.formatDate(requestTime) + "]";
	}
 
	 
	
}
