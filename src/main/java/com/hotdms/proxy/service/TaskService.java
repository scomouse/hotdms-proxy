package com.hotdms.proxy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotdms.proxy.bean.Task;
import com.hotdms.proxy.bean.TaskLog;
import com.hotdms.proxy.bean.TaskLogSearch;
import com.hotdms.proxy.init.TaskSchedulingConfigurer;
import com.hotdms.proxy.mapper.TaskLogMapper;
import com.hotdms.proxy.mapper.TaskMapper;
import com.hotdms.proxy.web.IdWorker;
import com.hotdms.proxy.web.Pagination;

@Service
@Transactional
public class TaskService {
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private TaskLogMapper taskLogMapper;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TaskSchedulingConfigurer scheduleConfigurer;
	
	public List<Task> findAll() {
		return taskMapper.findAll();
	}
	
	public Task findById(long id) {
		return taskMapper.findById(id);
	}
	
	public Task findByAlias(String alias) {
		return taskMapper.findByAlias(alias);
	}
	
	public void insert(Task task) {
		task.setId(idWorker.nextId());   
		taskMapper.insert(task); 
		scheduleConfigurer.start(task);
	}
	
	public void delete(Task task) {
		taskMapper.deleteById(task.getId()); 
		scheduleConfigurer.remove(task);
	}
	
	public TaskLog findLogById(Long id) {
		return taskLogMapper.findById(id);
	}
	
	public void updateById(Task task) {
		taskMapper.updateById(task);
		scheduleConfigurer.modify(task);
	}
	
	public void insert(TaskLog taskLog) {
		taskLogMapper.insert(taskLog); 
	}

	public Pagination<TaskLog> page(TaskLogSearch search) {
		int totalcount = taskLogMapper.count(search);
    	Pagination<TaskLog> pagination = new Pagination<TaskLog>(search.getPageNo(), totalcount);
    	List<TaskLog> userList = taskLogMapper.page(search);
    	pagination.setList(userList);
    	return pagination;
	}

}
