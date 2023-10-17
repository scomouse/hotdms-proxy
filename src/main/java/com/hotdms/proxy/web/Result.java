package com.hotdms.proxy.web;

import java.io.Serializable;

public class Result<T> implements Serializable {
	
	private static final long serialVersionUID = -2645479890175497992L;

	private String code;
	
	private int status;
	
	private String message;
	
	private T data;
	
	public static Result<Object> SUCCESS = new Result<Object>(WebUtils.CODE_SUCCESS, null); 
	
	public Result(String code, String message) {
		this.code = code;
		this.status = WebUtils.CODE_SUCCESS.equals(code) ? 1 : 0;
		this.message = message;
	}
	
	public Result(String code, String message, T data) {
		this.code = code;
		this.status = WebUtils.CODE_SUCCESS.equals(code) ? 1 : 0;
		this.message = message;
		this.data = data;
	}
	
	public static <T> Result<T> failure(String message) {
		return new Result<T>(WebUtils.CODE_FAILURE, message);
	}
	
	public static <T> Result<T> failure(String code, String message) {
		return new Result<T>(code, message);
	}
	
	public static <T> Result<T> success(String code, String message, T data) {
		return new Result<T>(code, message, data);
	}
	
	public static <T> Result<T> success(String message) {
		return new Result<T>(WebUtils.CODE_SUCCESS, message);
	}
	
	public static <T> Result<T> success(String message, T data) {
		return new Result<T>(WebUtils.CODE_SUCCESS, message, data);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
