package com.hotdms.proxy.init;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.hotdms.proxy.bean.Netway;
import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.cache.NetwayCache;
import com.hotdms.proxy.cache.ProxyCache;
import com.hotdms.proxy.schedule.ClearProxyScheduleTask;
import com.hotdms.proxy.service.NetwayService;
import com.hotdms.proxy.service.ProxyService;

@Configuration
@Order(-1000)
public class WebInitListener implements ApplicationListener<SpringApplicationEvent> { 
	
	private static Logger logger = LoggerFactory.getLogger(WebServiceConfig.class); 
	
	@Autowired
	private ProxyService proxyService;
	
	@Autowired
	private NetwayService netwayService;
	
	@Autowired
	private NetwayCache netwayCache; 
	
	@Autowired
	private ProxyCache proxyCache;
	
	@Autowired
	private TaskSchedulingConfigurer taskSchedulingConfigurer;
	
	@Override
	public void onApplicationEvent(SpringApplicationEvent event) { 
		
		List<Proxy> proxyLastList = proxyService.fetchLast(new Date( System.currentTimeMillis() - ClearProxyScheduleTask.CLEAR_EXPIRED ));
		logger.info("加载数据库已有代理" + proxyLastList.size() + "个");
		proxyCache.addProxy(proxyLastList); 
		
		List<Netway> netwayList = netwayService.findAll();
		logger.info("加载数据库已有通路配置" + netwayList.size() + "个"); 
		netwayCache.putAll(netwayList );
		 
		logger.info("准备启动定时任务"); 
		taskSchedulingConfigurer.startAll();
		
		// System.out.println("spring event : " + event.getClass().getName()); 	
		System.out.println("服务器已启动"); 	
	}
	
	 
	

}
