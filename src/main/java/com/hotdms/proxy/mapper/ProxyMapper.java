package com.hotdms.proxy.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.bean.ProxySearch;

public interface ProxyMapper {
	
	Proxy findById(@Param("id")Long id); 
	
	Proxy findByIpAndPort(@Param("ip")String ip, @Param("port")int port); 
	
    void deleteById(@Param("id")Long id);
    
    void deleteByFlag(@Param("checkTime")Date checkTime);
     
    void insert(Proxy proxy); 

    void updateById(Proxy proxy);
    
    List<Proxy> page(ProxySearch search);
    
    List<Proxy> fetchCheck(@Param("checkTime")Date checkTime);
    
    List<Proxy> fetchLast(@Param("checkTime")Date checkTime);
 
    int count(ProxySearch search);
    
}