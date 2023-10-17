package com.hotdms.proxy.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.CrawlerSearch;

public interface CrawlerMapper {
	
	Crawler findById(@Param("id")Long id); 
	
	Crawler findByName(@Param("name")String name); 
	
	List<Crawler> findAll();
	
	List<Crawler> findByTaskGroup(@Param("taskGroup")String taskGroup);
	
    void deleteById(@Param("id")Long id);
      
    void insert(Crawler crawler); 

    void updateById(Crawler crawler);
    
    void updateForCrawlTime(@Param("id")Long id, @Param("lastCrawlTime")Date lastCrawlTime);
    
    List<Crawler> page(CrawlerSearch search);
 
    int count(CrawlerSearch search);
    
}