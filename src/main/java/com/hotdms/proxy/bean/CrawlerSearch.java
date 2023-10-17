package com.hotdms.proxy.bean;

import com.hotdms.proxy.web.Page;

// 爬虫页面搜索类
public class CrawlerSearch extends Page {
	
	// 爬虫名称
	private String name; 
	
	// 状态
	private String status;
	
	// 执行任务线程
	private String taskGroup;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}
	 

	 
	 
}
