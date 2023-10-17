package com.hotdms.proxy.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Task;
import com.hotdms.proxy.bean.TaskLog;
import com.hotdms.proxy.service.ProxyService;
import com.hotdms.proxy.web.WebUtils;

@Component
public class ClearProxyScheduleTask {
	
	private static Logger logger = LoggerFactory.getLogger(ClearProxyScheduleTask.class); 

	@Autowired
	private ProxyService proxyService;
	 
	
	public static final Long CLEAR_EXPIRED = 1000 * 60 * 60 * 2L;
	 
	
	public JSONObject run(Task task) {
		JSONObject obj = new JSONObject(); 
		try {
			logger.info("开始执行无效代理清理任务");
			long time = System.currentTimeMillis() - CLEAR_EXPIRED;
			proxyService.deleteByFlag(new Date(time));
		} catch (Throwable e) {
			logger.error("统计订单立项文件数据信息时出现异常", e);
			obj.put("error", e.getMessage()); 
			obj.put("status", WebUtils.STATUS_ERROR); 
		}
		return obj;		
	} 
	
}
