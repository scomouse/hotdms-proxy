package com.hotdms.proxy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hotdms.proxy.bean.Netway;

public interface NetwayMapper {
	
	Netway findById(@Param("id")Long id); 
	
	Netway findByName(@Param("name")String name); 
	
	List<Netway> findAll(); 
	
    void deleteById(@Param("id")Long id);
      
    void insert(Netway netway); 

    void updateById(Netway netway); 
    
}