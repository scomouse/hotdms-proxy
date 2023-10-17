package com.hotdms.proxy.bean;

// 通路配置类
public class Netway implements Comparable {

	private long id;
	
	// 通路名称
	private String name;
	
	// 检测地址
	private String url;
	
	// 权重
	private int weight;
	
	// 通路有效状态码
	private String passCode; 
	
	// 状态 0 正常 1禁用
	private String status;
	
	// 顺序号
	private int sort;

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

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(Object o) {
		if( o == null) return 1;
		return sort - ((Netway)o).sort;
	} 
}
