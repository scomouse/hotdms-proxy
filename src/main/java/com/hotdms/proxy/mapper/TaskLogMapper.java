package com.hotdms.proxy.mapper;

import java.util.List;

import com.hotdms.proxy.bean.TaskLog;
import com.hotdms.proxy.bean.TaskLogSearch;

public interface TaskLogMapper {
	
	TaskLog findById(Long id);

	void insert(TaskLog taskLog);

	List<TaskLog> page(TaskLogSearch search);

	int count(TaskLogSearch search); 
}