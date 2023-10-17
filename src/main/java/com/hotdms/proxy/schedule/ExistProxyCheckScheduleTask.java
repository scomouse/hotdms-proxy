package com.hotdms.proxy.schedule;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.bean.Task;
import com.hotdms.proxy.service.ProxyCrawlerService;
import com.hotdms.proxy.service.ProxyService;
import com.hotdms.proxy.web.WebUtils;

@Component
public class ExistProxyCheckScheduleTask {
	
	private static Logger logger = LoggerFactory.getLogger(ExistProxyCheckScheduleTask.class); 

	@Autowired
	private ProxyService proxyService;
	
	@Autowired
	private ProxyCrawlerService proxyCrawlerService;
	
	private static final Long CHECK_EXPIRED = 1000 * 60 * 30L;
	 
	
	public JSONObject run(Task task) {
		JSONObject obj = new JSONObject(); 
		try {
			logger.info("开始执行代理有效性检测任务");
			long time = System.currentTimeMillis() - CHECK_EXPIRED;
			List<Proxy> proxyList = proxyService.fetchCheck(new Date(time)); 
			if(proxyList == null || proxyList.size() == 0) {
				logger.info("本次任务在数据库中没有找到需要检测的代理"); 
			} else {
				logger.info("本次从数据库中提取到" + proxyList.size() + "个待检测代理数据，准备检测可用性");
				int count = proxyCrawlerService.handleExists(proxyList.toArray(new Proxy[0]));
				logger.info("完成代理有效性检测任务，其中" + count + "/" + proxyList.size() + "个代理可用");
			}  
		} catch (Throwable e) {
			logger.error("统计订单立项文件数据信息时出现异常", e);
			obj.put("error", e.getMessage()); 
			obj.put("status", WebUtils.STATUS_ERROR); 
		}
		return obj;		
	} 
	
}
