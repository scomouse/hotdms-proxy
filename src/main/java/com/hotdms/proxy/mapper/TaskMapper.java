package com.hotdms.proxy.mapper;
 
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hotdms.proxy.bean.Task;

public interface TaskMapper {
	
	List<Task> findAll(); 
	
	Task findById(@Param("id")Long id);
	
	Task findByAlias(@Param("alias")String alias); 
	
	void updateById(Task task);
	
	void insert(Task task);
	
	void deleteById(@Param("id")Long id);
}