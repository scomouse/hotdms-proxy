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

import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.CrawlerSearch;
import com.hotdms.proxy.service.CrawlerService;
import com.hotdms.proxy.web.Pagination;
import com.hotdms.proxy.web.Result;
import com.hotdms.proxy.web.WebUtils; 

@Controller
@RequestMapping("/crawler")
public class CrawlerController {
	
	private static Logger logger = LoggerFactory.getLogger(CrawlerController.class);
	
	@Autowired
	private CrawlerService crawlerService;  
 
	@ResponseBody
	@RequestMapping({"/list"})
	public Result<Object> list(HttpServletRequest request, HttpServletResponse response, @RequestBody CrawlerSearch search) {
		try { 
			Pagination<Crawler> pagination =  crawlerService.page(search);
			return Result.success(null, pagination);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping({"/modify"})
	public Result<Object> modify(HttpServletRequest request, HttpServletResponse response, @RequestBody Crawler crawler) {
		try { 
			Crawler exists = crawlerService.findById(crawler.getId());
			if(exists == null) {
				return Result.failure("未找到指定ID的爬虫");
			} 
			Crawler existsName = crawlerService.findByName(crawler.getName());
			if(existsName!= null && existsName.getId() != crawler.getId()) {
				return Result.failure( "同名爬虫已存在");
			} 
			crawlerService.updateById(crawler); 
			return Result.success("修改成功", crawler);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping({"/add"})
	public Result<Object> add(HttpServletRequest request, HttpServletResponse response, @RequestBody Crawler crawler) {
		try {
			Crawler exists = crawlerService.findByName(crawler.getName());
			if(exists!= null) {
				return Result.failure( "同名爬虫已存在");
			}  
			crawlerService.insert(crawler); 
			return Result.success("添加成功", crawler);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	 
	@ResponseBody
	@RequestMapping({"/remove"})
	public Result<Object> remove(HttpServletRequest request, HttpServletResponse response, String crawlerids) {
		try { 
			List<Long> ids = WebUtils.toLongList(crawlerids, ",");  
			for(Long id : ids) {
				crawlerService.deleteById(Long.valueOf(id));
			}
			return Result.success("删除成功", null);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	 
}
