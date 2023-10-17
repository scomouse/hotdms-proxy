package com.hotdms.proxy.bean;

import com.hotdms.proxy.web.Page;

// 任务日志搜索类
public class TaskLogSearch extends Page {
	
	// 任务ID
	private Long taskid;
	
	// 执行结果状态
	private String status;

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
