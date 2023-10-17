package com.hotdms.proxy.bean;

import java.util.Date;
import java.util.Objects;

// 代理数据
public class Proxy {
	
	private Long id;
	
	// 代理IP地址
	private String ip;
	
	// 代理端口号
	private int port;
	
	// 所属位置
	private String address;
	
	// 速度
	private int speed;
	
	// 通路
	private int ratio;
	
	// 检测时间
	private Date checkTime;
	
	// 加入时间
	private Date createTime;
	
	// 状态
	private String status;
	
	// 来源 - 爬虫名称
	private String source;
	
	// 删除标记
	private int flag;

	@Override
	public boolean equals(Object obj) { 
		if (obj == null) return false; 
		Proxy other = (Proxy) obj;
		return ip.equals(other.ip) && port == other.port;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	

}
