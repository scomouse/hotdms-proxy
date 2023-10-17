package com.hotdms.proxy.web;

import java.util.List;

@SuppressWarnings("serial")
public class Pagination<T> implements java.io.Serializable {
	
	private int pageSize = 10;
	
	private int pageNo; // 从一开始计数
	
	private int totalCount;
	
	private List<T> list;
	  
	public Pagination(int pageNo, int pageSize, int totalCount) {
		this.pageNo = ((pageNo > 0)? pageNo : 1);
		this.pageSize = pageSize;
		this.totalCount = totalCount;
	}
	
	public Pagination(int pageNo, int totalCount) {
		this.pageNo = ((pageNo > 0)? pageNo : 1);
		this.totalCount = totalCount;
	} 
	 
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = ((pageNo > 0)? pageNo : 1);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	public int getStart() {
		return pageSize * (pageNo - 1);
	} 
}
