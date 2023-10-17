package com.hotdms.proxy.crawler;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Crawler;
import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.client.HttpSession;
import com.hotdms.proxy.web.WebUtils;

// 基于XPath的纯网页爬虫实现类
public class XPathWebPageCrawler implements ProxyCrawler {
	
	private static Logger logger = LoggerFactory.getLogger(XPathWebPageCrawler.class);
	
	private Crawler crawler;

	private static long lastCrawlTime = 0L;
	
	public XPathWebPageCrawler(Crawler crawler) {
		this.crawler = crawler;
	}
	
	@Override
	public Proxy[] findProxy() {
		HttpSession session = new HttpSession();
		logger.info("抓取网页" + crawler.getUrl());
		ArrayList<Proxy> proxyList = new ArrayList<Proxy>();
		
		String findPageParams = WebUtils.findMatch(crawler.getUrl());
		int startPage = 0, endPage = 0;
		if(findPageParams!=null) {
			String[] pages = findPageParams.substring(1, findPageParams.length() -1).split("-");
			startPage = Integer.parseInt(pages[0]);
			endPage = Integer.parseInt(pages[1]);
		}
		
		for(int p=startPage; p <= endPage; p++) {
			try {
				String pageUrl = findPageParams!=null ? 
						crawler.getUrl().replace(findPageParams, ""+ p) : crawler.getUrl();
				JSONObject result = session.sendGet(pageUrl, "UTF-8", new JSONObject());
				if(result == null || result.getIntValue("code") != 200) {
					logger.error("抓取网页" + pageUrl + "时未获得正确响应");
					continue ;
				}
				Document doc = Jsoup.parse(result.getString("content"));
				Elements rows = doc.selectXpath(crawler.getXpath()); 
				String[] keys = crawler.getKeymap().split(","); 
				for(int i=0; i< rows.size(); i++) {
					Element row = rows.get(i);
					Elements cols = row.getElementsByTag("td"); 
					
					JSONObject obj = new JSONObject();
					for(int k=0; k< keys.length; k++) {
						if(keys[k]!= null && keys[k].length() > 0) {
							obj.put(keys[k], cols.get(k).text());
						}
					} 
					Proxy proxy = obj.toJavaObject(Proxy.class);
					proxy.setSource(crawler.getName());
					proxyList.add(proxy);
				} 
			} catch(Throwable e) {
				
			}
		}
		session.close();
		logger.info("本次访问发现网页上共有" + proxyList.size() + "个代理数据，准备检测可用性");  
		crawler.setLastCrawlTime(new Date(System.currentTimeMillis()));
		return proxyList.toArray(new Proxy[0]);
	}
	
	@Override
	public Crawler getCrawler() {
		return crawler;
	}

	@Override
	public boolean canCrawl() { 
		Date lastTime = crawler.getLastCrawlTime();
		return System.currentTimeMillis() - (lastTime!=null? lastTime.getTime() : 0) >= crawler.getTimeInterval();
	}

}
