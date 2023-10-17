package com.hotdms.proxy.action;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.bean.ProxySearch;
import com.hotdms.proxy.bean.ProxyTest;
import com.hotdms.proxy.cache.ProxyCache;
import com.hotdms.proxy.service.ProxyCrawlerService;
import com.hotdms.proxy.service.ProxyService;
import com.hotdms.proxy.web.ApiResult;
import com.hotdms.proxy.web.Pagination;
import com.hotdms.proxy.web.Result; 

@Controller
@RequestMapping("/proxy")
public class ProxyController {
	
	private static Logger logger = LoggerFactory.getLogger(ProxyController.class);
	
	@Autowired
	private ProxyService proxyService;   
	
	@Autowired
	private ProxyCrawlerService proxyCrawlerService;   
	
	@Autowired
	private ProxyCache proxyCache; 
 
	@ResponseBody
	@RequestMapping({"/list"})
	public Result<Object> list(HttpServletRequest request, HttpServletResponse response, @RequestBody ProxySearch search) {
		try {
			search.setFlag(0);
			Pagination<Proxy> pagination =  proxyService.page(search);
			return Result.success(null, pagination);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	} 
	
	@ResponseBody
	@RequestMapping({"/status"})
	public Result<Object> status(HttpServletRequest request, HttpServletResponse response, @RequestBody Proxy proxy) {
		try {
			Proxy exists = proxyService.findById(proxy.getId());
			if(exists == null) {
				return Result.failure( "未找到指定ID的代理");
			}
			exists.setStatus(proxy.getStatus());
			proxyService.updateById(exists);
			proxyCache.removeProxy(exists);
			
			return Result.success("修改成功", exists);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping({"/test"})
	public Result<Object> status(HttpServletRequest request, HttpServletResponse response, @RequestBody ProxyTest test) {
		try {
			Proxy proxy = proxyService.findById(test.getId());
			if(proxy == null) {
				return Result.failure( "未找到指定ID的代理");
			}  
			JSONObject obj = proxyCrawlerService.checkProxy(test.getUrl(), proxy);
			return Result.success("修改成功", obj);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	 
	@ResponseBody
	@RequestMapping({"/fetch/last"})
	public ApiResult<Object> fetchLast(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Proxy> proxyList = new ArrayList<Proxy>();
			Proxy proxy = proxyCache.fetchLast();
			proxyList.add(proxy);
			return ApiResult.success("查询成功", proxyList);
		} catch(Exception e) {
			return ApiResult.failure(e.getMessage());
		}
	}  
	
	@ResponseBody
	@RequestMapping({"/fetch/random"})
	public ApiResult<Object> fetchRandom(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Proxy> proxyList = new ArrayList<Proxy>();
			Proxy proxy = proxyCache.fetchRandom();
			proxyList.add(proxy);
			return ApiResult.success("查询成功", proxyList);
		} catch(Exception e) {
			return ApiResult.failure(e.getMessage());
		}
	} 
	 
}
