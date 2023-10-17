package com.hotdms.proxy.bean;

import java.util.Date;

import com.hotdms.proxy.web.Page;

// 爬虫搜索查询类
public class ProxySearch extends Page {
	
	private String ip;
	
	// 类别 空 - 全部  SOCK \ HTTP
	private String type;
	
	// 所属位置
	private String address;
	
	// 速度
	private int speed; 
	
	// 删除标记
	private Integer flag; 
	 
	// 状态
	private String status;
	
	// 来源
	private String source;
	
	// 检测起始时间
	private Date startCheckTime;
	
	// 检测结束时间
	private Date endCheckTime; 

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartCheckTime() {
		return startCheckTime;
	}

	public void setStartCheckTime(Date startCheckTime) {
		this.startCheckTime = startCheckTime;
	}

	public Date getEndCheckTime() {
		return endCheckTime;
	}

	public void setEndCheckTime(Date endCheckTime) {
		this.endCheckTime = endCheckTime;
	}
	
	 

	 
}
