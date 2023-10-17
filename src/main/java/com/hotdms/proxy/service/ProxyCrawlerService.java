package com.hotdms.proxy.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.Netway;
import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.cache.NetwayCache;
import com.hotdms.proxy.cache.ProxyCache;
import com.hotdms.proxy.client.HttpSession;
import com.hotdms.proxy.client.SSLClient;
import com.hotdms.proxy.crawler.ProxyCrawler;
import com.hotdms.proxy.web.IdWorker;
import com.hotdms.proxy.web.WebUtils; 

@Service
public class ProxyCrawlerService {

	private static Logger logger = LoggerFactory.getLogger(ProxyCrawlerService.class);
	
	@Autowired
	private ProxyService proxyService;
	
	@Autowired
	private CrawlerService crawlerService;
	
	@Autowired
	private ProxyCache proxyCache; 
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private NetwayCache netwayCache;
	
	 
	// 来自网页爬虫的代理，先检测是否存在，已存在则不处理（已存在的应该由代理有效性检测任务进行维护处理）
	public int handleProxy(ProxyCrawler proxyCrawler) {
		Proxy[] proxyList = null;
		Crawler crawler = proxyCrawler.getCrawler();
		try {
			proxyList = proxyCrawler.findProxy();
			crawlerService.updateForCrawlTime(crawler.getId(), crawler.getLastCrawlTime());	
		} catch(Throwable e) {
			logger.warn("代理爬虫" + crawler.getName() + "获取代理时出现异常", e);
		}
		
		if(proxyList == null || proxyList.length == 0) {
			return 0;
		}
		Netway[] netways = netwayCache.getAll();
		int count = 0;
		for(Proxy proxy : proxyList) {
			Proxy exists = proxyService.findByIpAndPort(proxy.getIp(), proxy.getPort());
			if(exists == null) {
				proxy.setId(idWorker.nextId());
				proxy.setStatus("0");
				proxy.setCreateTime(new Date());
				checkProxy(netways, proxy);
				proxy.setCheckTime(new Date()); 
				if(proxy.getFlag() == 0) {
					proxyCache.addProxy(proxy);
					proxyService.insert(proxy); 
					count++;
				}
			}
		}
		return count; 
	}

	// 此方法为代理有效性检测任务调用，仅针对已存在的代理
	public int handleExists(Proxy[] proxyList) {
		int count = 0;
		Netway[] netways = netwayCache.getAll();
		for(Proxy proxy : proxyList) {
			checkProxy(netways, proxy);
			proxy.setCheckTime(new Date());
			saveExists(proxy);
			if(proxy.getFlag() == 0) {
				count++;
			}
		}
		return count;
	}


	private void saveExists(Proxy proxy) {
		if(proxy.getFlag() == 1) {
			proxyCache.removeProxy(proxy);
			proxyService.deleteById(proxy.getId());	
		} else {
			proxyCache.addProxy(proxy);
			proxyService.updateById(proxy);
		}
	}
	
	private void checkProxy(Netway[] netways, Proxy proxy) {
		HttpSession session =   new HttpSession(SSLClient.createSSLProxyCheckClient(proxy.getIp(), proxy.getPort()));
		long totalTime = 0L;
		int ratio = 0, count = 0;
		for(int i =0; i< netways.length; i++) {
			try {
				// 判断通路检测是否启用
				if(!WebUtils.STATUS_OK.equals(netways[i].getStatus())) {
					continue;
				}
				// 通过代理进行访问通路
				long time = System.currentTimeMillis();
				JSONObject result = session.sendGet(netways[i].getUrl(), "UTF-8", new JSONObject());
				// 如果无响应或者响应码不是有效的响应码
				if(result == null ||
						WebUtils.indexOf(netways[i].getPassCode().split(","), result.getString("code")) == -1 ) {
					logger.info(proxy.getIp() + ":" + proxy.getPort() + "请求" + netways[i].getUrl() + "时无效响应，响应码：" +
							result.getIntValue("code"));
					continue;
				}
				totalTime += (System.currentTimeMillis() - time);
				ratio = ratio + netways[i].getWeight();
				count++;
			} catch(Throwable e) {
				logger.info(proxy.getIp() + ":" + proxy.getPort() + "请求" + netways[i].getUrl() + "时发生异常");
			}
		}
		if(count == 0) {
			// 标记为删除，等待清理线程处理
			proxy.setFlag(1);
			logger.info( proxy.getIp() + ":" + proxy.getPort() + " 已被诊断为无效代理");
		} else {
			proxy.setRatio(ratio);
			proxy.setSpeed((int) Math.floor(totalTime / count));
			proxy.setFlag(0);
			logger.info( proxy.getIp() + ":" + proxy.getPort() + " 通路 " + proxy.getRatio() + " 平均速度 " + proxy.getSpeed() );
		}
	}
	
	public JSONObject checkProxy(String url, Proxy proxy) {
		HttpSession session =   new HttpSession(SSLClient.createSSLProxyCheckClient(proxy.getIp(), proxy.getPort()));
		long time = System.currentTimeMillis();
		try {
			JSONObject response = session.sendGet(url, "UTF-8", new JSONObject());
			response.put("time", (System.currentTimeMillis() - time));
			return response;
		} catch(Throwable e) {
			JSONObject obj = new JSONObject();
			obj.put("code", -1);
			obj.put("message", e.getMessage());
			obj.put("time", (System.currentTimeMillis() - time));
			return obj;
		}
	}

}
