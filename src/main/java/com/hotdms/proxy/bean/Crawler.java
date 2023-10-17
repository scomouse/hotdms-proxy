package com.hotdms.proxy.bean;

import java.util.Date;

// 爬虫配置类
public class Crawler {
	
	private long id;
	
	// 爬虫名称
	private String name;
	
	// 爬虫地址
	private String url;
	
	// 爬虫规则路径
	private String xpath;

	// 爬取间隔时间
	private int timeInterval;
	
	// 字段提取映射
	private String keymap;
	
	// 爬虫处理类名
	private String handlerClass;
	
	// 爬虫执行任务线程
	private String taskGroup;
	
	// 状态 0 正常 1 禁用
	private String status;
	
	// 配置更新时间
	private Date updateTime;
	
	// 最后爬取时间
	private Date lastCrawlTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getKeymap() {
		return keymap;
	}

	public void setKeymap(String keymap) {
		this.keymap = keymap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getLastCrawlTime() {
		return lastCrawlTime;
	}

	public void setLastCrawlTime(Date lastCrawlTime) {
		this.lastCrawlTime = lastCrawlTime;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	} 
	
}
