package com.hotdms.proxy.bean;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

// 任务日志类
public class TaskLog {
	
	private long id;
	
	// 任务ID
	private long taskid;
	
	// 记录时间
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date time;
	
	// 执行结果状态
	private String status;
	
	// 执行日志信息
	private String message; 
	
	public TaskLog() {
		
	}  
	
	public static TaskLog create(long taskid, Date time, JSONObject result) {
		TaskLog instance = new TaskLog();
		instance.taskid = taskid;
		instance.time = time;
		instance.status = result.getString("status");
		instance.message =result.toJSONString();  
		return instance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTaskid() {
		return taskid;
	}

	public void setTaskid(long taskid) {
		this.taskid = taskid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
