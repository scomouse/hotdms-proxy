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

import com.hotdms.proxy.bean.Netway;
import com.hotdms.proxy.service.NetwayService;
import com.hotdms.proxy.web.Pagination;
import com.hotdms.proxy.web.Result;
import com.hotdms.proxy.web.WebUtils; 

@Controller
@RequestMapping("/netway")
public class NetwayController {
	
	private static Logger logger = LoggerFactory.getLogger(NetwayController.class);
	
	@Autowired
	private NetwayService netwayService;   
 
	@ResponseBody
	@RequestMapping({"/list"})
	public Result<Object> list(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Netway> netwayList =  netwayService.findAll(); 
			Pagination<Netway> pagination =  new Pagination<Netway>(1, 100, netwayList.size());
			pagination.setList(netwayList);
			return Result.success(null, pagination);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	 
	@ResponseBody
	@RequestMapping({"/modify"})
	public Result<Object> modify(HttpServletRequest request, HttpServletResponse response, @RequestBody Netway netway) {
		try { 
			Netway exists = netwayService.findById(netway.getId());
			if(exists == null) {
				return Result.failure( "未找到指定ID的通路配置");
			} 
			Netway existsName = netwayService.findByName(netway.getName());
			if(existsName!= null && existsName.getId() != netway.getId()) {
				return Result.failure( "同名通路配置已存在");
			} 
			netwayService.updateById(netway);  
			return Result.success("修改成功", netway);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping({"/add"})
	public Result<Object> add(HttpServletRequest request, HttpServletResponse response, @RequestBody Netway netway) {
		try {
			Netway exists = netwayService.findByName(netway.getName());
			if(exists!= null) {
				return Result.failure( "同名爬虫已存在");
			} 
			netwayService.insert(netway);  
			return Result.success("添加成功", netway);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	 
	@ResponseBody
	@RequestMapping({"/remove"})
	public Result<Object> remove(HttpServletRequest request, HttpServletResponse response, String netwayids) {
		try { 
			List<Long> ids = WebUtils.toLongList(netwayids, ",");  
			for(Long id : ids) {
				Netway netway = netwayService.findById(id);
				if(netway != null) {
					netwayService.delete(netway);	
				} 
			}
			return Result.success("删除成功", null);
		} catch(Exception e) {
			return Result.failure(e.getMessage());
		}
	}
	
	 
}
