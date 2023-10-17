package com.hotdms.proxy.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.bean.ProxySearch;
import com.hotdms.proxy.mapper.ProxyMapper;
import com.hotdms.proxy.web.Pagination;

@Service
@Transactional
public class ProxyService {
		
	@Autowired
	private ProxyMapper proxyMapper;
	
	public Proxy findById(Long id) {
		return proxyMapper.findById(id);
	}
	
	public Proxy findByIpAndPort(String ip , int port) {
		return proxyMapper.findByIpAndPort(ip, port);
	}
	
	public void deleteById(Long id) {
		proxyMapper.deleteById(id);
    }
    
    public void deleteByFlag(Date checkTime) {
    	proxyMapper.deleteByFlag(checkTime);
    }
     
    public void insert(Proxy proxy) {
    	proxyMapper.insert(proxy);
    }

    public void updateById(Proxy proxy) {
    	proxyMapper.updateById(proxy);
    }
    
    public List<Proxy> fetchCheck(Date checkTime) {
    	return proxyMapper.fetchCheck(checkTime);
    }
    
    public List<Proxy> fetchLast(Date checkTime) {
    	return proxyMapper.fetchLast(checkTime);
    }
     
	public Pagination<Proxy> page(ProxySearch search) { 
    	int totalcount = proxyMapper.count(search);
    	Pagination<Proxy> pagination = new Pagination<Proxy>(search.getPageNo(), search.getPageSize(), totalcount);
    	List<Proxy> proxyList = proxyMapper.page(search);
    	pagination.setList(proxyList);
    	return pagination;
    }  
}
