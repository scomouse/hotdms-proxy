package com.hotdms.proxy.schedule;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.Task;
import com.hotdms.proxy.crawler.ProxyCrawler;
import com.hotdms.proxy.service.CrawlerService;
import com.hotdms.proxy.service.ProxyCrawlerService;
import com.hotdms.proxy.web.WebUtils; 

@Component
public class ProxyCrawlerScheduleTask {
	
	private static Logger logger = LoggerFactory.getLogger(ProxyCrawlerScheduleTask.class);
	
	@Autowired
	private ProxyCrawlerService proxyCrawlerService;
	
	@Autowired
	private CrawlerService crawlerService;
	
	public JSONObject run(Task task) {
		Date startTime = new Date();
		JSONObject obj = new JSONObject();
		int count = 0;
		try {
			List<Crawler> crawlerList = crawlerService.findByTaskGroup(task.getAlias());
			logger.info("开始执行代理爬虫任务");
			 for(Crawler crawler : crawlerList) {
				 if(!WebUtils.STATUS_OK.equals(crawler.getStatus())) {
					continue ; 
				 }
				 String handlerClass = crawler.getHandlerClass();
				 if(!handlerClass.contains(".")) {
					 handlerClass = "com.hotdms.proxy.crawler." + handlerClass;
				 }
				 Class<ProxyCrawler> instance = (Class<ProxyCrawler>) Class.forName(handlerClass);
				 Constructor<ProxyCrawler> constructor = instance.getDeclaredConstructor(new Class[] { Crawler.class });
				 ProxyCrawler proxyCrawler = constructor.newInstance(crawler);
				 if(proxyCrawler.canCrawl()) {
					 count += proxyCrawlerService.handleProxy(proxyCrawler);
				 }
			 }
			logger.info("完成代理爬虫任务，本次共找到了" + count + "个代理");
			obj.put("needLog", (count > 0));
		} catch (Throwable e) {
			logger.error("统计订单立项文件数据信息时出现异常", e);
			obj.put("error", e.getMessage()); 
			obj.put("status", WebUtils.STATUS_ERROR); 
		}
		
		return obj;
			
	} 
}
