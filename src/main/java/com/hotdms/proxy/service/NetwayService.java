package com.hotdms.proxy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotdms.proxy.bean.Netway;
import com.hotdms.proxy.cache.NetwayCache;
import com.hotdms.proxy.mapper.NetwayMapper;
import com.hotdms.proxy.web.IdWorker;
 

@Service
@Transactional
public class NetwayService {
		
	@Autowired
	private NetwayMapper netwayMapper;
	
	@Autowired
	private NetwayCache netwayCache; 
	
	@Autowired
	private IdWorker idWorker;
	
	public Netway findById(Long id) {
		return netwayMapper.findById(id);
	}
	
	public Netway findByName(String name) {
		return netwayMapper.findByName(name);
	}
	
	public void delete(Netway netway) {
		netwayMapper.deleteById(netway.getId());
		netwayCache.remove(netway);	
    }
     
    public void insert(Netway netway) {
    	netway.setId(idWorker.nextId());  
    	netwayMapper.insert(netway);
		netwayCache.put(netway);
    }

    public void updateById(Netway netway) {
    	netwayMapper.updateById(netway);
    	netwayCache.put(netway);
    }
     
    public List<Netway> findAll() {
		return netwayMapper.findAll();
	}
}
