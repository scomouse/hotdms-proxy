package com.hotdms.proxy.cache;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.hotdms.proxy.bean.Netway; 

// 通路配置缓存类
public class NetwayCache { 
	
	// 缓存集合
	private HashMap<String, Netway> cacheData;
	
	// 处理锁
	private Object lock;
	
	public NetwayCache() { 
		 this.lock = new Object();
		 this.cacheData = new HashMap<String, Netway>();
	}
	
	 
	public void putAll(List<Netway> netwayList) {
		for(Netway netway : netwayList) {
			put(netway);
		}
	}
 
	public void put(Netway netway) {
		String key = netway.getName();
		 synchronized(lock) {
			 if(cacheData.containsKey(key)) {
				 cacheData.remove(key); 
			 }
			 cacheData.put(key, netway);
		 }
	} 
	
	public void remove(Netway netway) {
		String key = netway.getName();
		synchronized(lock) {
			cacheData.remove(key);
		}
	}
	
	public Netway[] getAll() {
		Netway[] dataArray = null;
		synchronized(lock) {
			dataArray = cacheData.values().toArray(new Netway[0]);
		}
		Arrays.sort(dataArray);
		return dataArray;	
	}
	
}
