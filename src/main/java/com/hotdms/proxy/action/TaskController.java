package com.hotdms.proxy.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotdms.proxy.bean.Task;
import com.hotdms.proxy.service.TaskService;
import com.hotdms.proxy.web.Pagination;
import com.hotdms.proxy.web.Result;
import com.hotdms.proxy.web.WebUtils; 

@Controller
@RequestMapping("/schedule")
public class TaskController {
	
	private static Logger logger = LoggerFactory.getLogger(TaskController.class);
	 
	@Autowired
	private TaskService taskService;   
	
	@ResponseBody
	@RequestMapping({"/list"})
	public Result<Object> list(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Task> taskList =  taskService.findAll(); 
			Pagination<Task> pagination =  new Pagination<Task>(1, 100, taskList.size());
			pagination.setList(taskList);
			return Result.success(null, pagination);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	 
	@ResponseBody
	@RequestMapping({"/modify"})
	public Result<Object> modify(HttpServletRequest request, HttpServletResponse response, @RequestBody Task task) {
		try { 
			Task exists = taskService.findById(task.getId());
			if(exists == null) {
				return Result.failure( "未找到指定ID的计划任务");
			} 
			Task existsName = taskService.findByAlias(task.getAlias());
			if(existsName!= null && existsName.getId() != task.getId()) {
				return Result.failure( "同名计划任务已存在");
			} 
			taskService.updateById(task);  
			return Result.success("修改成功", task);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping({"/add"})
	public Result<Object> add(HttpServletRequest request, HttpServletResponse response, @RequestBody Task task) {
		try {
			Task exists = taskService.findByAlias(task.getAlias());
			if(exists!= null) {
				return Result.failure( "同名计划任务已存在");
			}  
			taskService.insert(task); 
			return Result.success("添加成功", task);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	 
	@ResponseBody
	@RequestMapping({"/remove"})
	public Result<Object> remove(HttpServletRequest request, HttpServletResponse response, String taskids) {
		try { 
			List<Long> ids = WebUtils.toLongList(taskids, ",");  
			for(Long id : ids) {
				Task task = taskService.findById(id);
				if(task != null) {
					taskService.delete(task);
				}
			}
			return Result.success("删除成功", null);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	 
}
