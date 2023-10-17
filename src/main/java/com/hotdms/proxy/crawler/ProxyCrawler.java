package com.hotdms.proxy.crawler;

import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.Proxy;

// 代理爬虫接口
public interface ProxyCrawler {
	
	Proxy[] findProxy();
	
	boolean canCrawl();
	
	Crawler getCrawler();
 
}
