package com.hotdms.proxy.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.CrawlerSearch;
import com.hotdms.proxy.mapper.CrawlerMapper;
import com.hotdms.proxy.web.IdWorker;
import com.hotdms.proxy.web.Pagination;
 

@Service
@Transactional
public class CrawlerService {
		
	@Autowired
	private CrawlerMapper crawlerMapper;
	
	@Autowired
	private IdWorker idWorker;
	
	public Crawler findById(Long id) {
		return crawlerMapper.findById(id);
	}
	
	public Crawler findByName(String name) {
		return crawlerMapper.findByName(name);
	}
	
	public void deleteById(Long id) {
		crawlerMapper.deleteById(id);
    }
     
    public void insert(Crawler crawler) {
    	crawler.setId(idWorker.nextId());
    	crawler.setUpdateTime(new Date());
    	crawlerMapper.insert(crawler);
    }

    public void updateById(Crawler crawler) {
    	crawler.setUpdateTime(new Date());
    	crawlerMapper.updateById(crawler);
    }
     
    public List<Crawler> findAll() {
		return crawlerMapper.findAll();
	} 
	
    public List<Crawler> findByTaskGroup(String taskGroup) {
    	return crawlerMapper.findByTaskGroup(taskGroup);
	}
    
    public void updateForCrawlTime(Long id, Date lastCrawlTime) {
    	crawlerMapper.updateForCrawlTime(id, lastCrawlTime);
    }
	
	public Pagination<Crawler> page(CrawlerSearch search) { 
    	int totalcount = crawlerMapper.count(search);
    	Pagination<Crawler> pagination = new Pagination<Crawler>(search.getPageNo(), search.getPageSize(), totalcount);
    	List<Crawler> crawlerList = crawlerMapper.page(search);
    	pagination.setList(crawlerList);
    	return pagination;
    }  
}
