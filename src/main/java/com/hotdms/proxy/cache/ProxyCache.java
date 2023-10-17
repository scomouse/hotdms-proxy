package com.hotdms.proxy.cache;

import java.util.ArrayList;
import java.util.List;

import com.hotdms.proxy.bean.Proxy;

// 代理提取缓存池
public class ProxyCache {
	
	// 代理缓存数据
	private List<Proxy> proxyList = new ArrayList<Proxy>();
	
	// 最大缓存数量
	private static final int MAX_SIZE = 20;
	
	public synchronized void addProxy(Proxy proxy) {		
		if(proxyList.size() >= MAX_SIZE) {
			proxyList.remove(0);
		}
		proxyList.remove(proxy);
		proxyList.add(proxy);
	}
	
	public synchronized void addProxy(List<Proxy> proxyList) {	
		for(Proxy proxy : proxyList) {
			addProxy(proxy);
		}
	}
	
	public synchronized Proxy fetchLast() {
		int count = proxyList.size();
		return count > 0 ? proxyList.get( count - 1 ) : null;
	}
	
	public synchronized Proxy fetchRandom() {
		int count = proxyList.size();
		return count > 0 ? proxyList.get((int)Math.floor( count * Math.random())) : null;
	}
	
	public synchronized void removeProxy(Proxy proxy) {
		proxyList.remove(proxy);
	} 
	
}
