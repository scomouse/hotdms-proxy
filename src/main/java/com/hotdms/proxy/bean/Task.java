package com.hotdms.proxy.bean;

// 计划任务类
public class Task {
	
	private long id;
	
	// 任务标题
	private String name;
	
	// 任务代号
	private String alias;
	
	// 任务执行类名
	private String beanName;
	
	// 任务cron表达式
	private String cronExp;
	
	// 备注
	private String remark; 
	
	// 状态 0 正常 1禁用
	private String status;
	
	public static final String STATUS_OK = "0";

	// 是否记录日志
	private boolean needLog;
	
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	} 
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isNeedLog() {
		return needLog;
	}

	public void setNeedLog(boolean needLog) {
		this.needLog = needLog;
	} 

}
