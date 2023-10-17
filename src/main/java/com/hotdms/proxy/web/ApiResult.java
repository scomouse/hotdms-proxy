package com.hotdms.proxy.web;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {
	
	private static final long serialVersionUID = -2645479890175497992L;

	private int code;
	
	private boolean success;
	
	private String msg;
	
	private T data;
	
	public static final int CODE_SUCCESS = 0; 
	
	public static final int CODE_FAILURE = -1;
	
	public static ApiResult<Object> SUCCESS = new ApiResult<Object>(CODE_SUCCESS, null);
	 
	
	public ApiResult(int code, String message) {
		this.code = code;
		this.success = (code == CODE_SUCCESS);
		this.msg = message;
	}
	
	public ApiResult(int code, String message, T data) {
		this.code = code;
		this.success = (code == CODE_SUCCESS);
		this.msg = message;
		this.data = data;
	}
	
	public static <T> ApiResult<T> failure(String message) {
		return new ApiResult<T>(CODE_FAILURE, message);
	}
	
	public static <T> ApiResult<T> failure(int code, String message) {
		return new ApiResult<T>(code, message);
	}
	
	public static <T> ApiResult<T> success(int code, String message, T data) {
		return new ApiResult<T>(code, message, data);
	}
	
	public static <T> ApiResult<T> success(String message) {
		return new ApiResult<T>(CODE_SUCCESS, message);
	}
	
	public static <T> ApiResult<T> success(String message, T data) {
		return new ApiResult<T>(CODE_SUCCESS, message, data);
	}
	  

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
 
}
