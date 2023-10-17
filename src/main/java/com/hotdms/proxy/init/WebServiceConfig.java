package com.hotdms.proxy.init;
 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hotdms.proxy.bean.Netway;
import com.hotdms.proxy.bean.Proxy;
import com.hotdms.proxy.cache.NetwayCache;
import com.hotdms.proxy.cache.ProxyCache;
import com.hotdms.proxy.schedule.ClearProxyScheduleTask;
import com.hotdms.proxy.service.AuthService;
import com.hotdms.proxy.service.NetwayService;
import com.hotdms.proxy.service.ProxyService;
import com.hotdms.proxy.web.IdWorker;

@Configuration
public class WebServiceConfig {
	
	@Autowired
	private AuthService authService; 
	
	
	@Bean(name= {"proxyCache"}) 
	public ProxyCache loadProxyCache() {
		return new ProxyCache();  
	}
	
	@Bean(name= {"netwayCache"}) 
	public NetwayCache loadNetwayCache() {
		return new NetwayCache();	 
	}
	
	@Bean(name= {"idWorker"}) 
	public IdWorker loadIdWorker() {
		return new IdWorker();
	}
 
	
	@Bean
	public FilterRegistrationBean accessTokenFilterRegistrationBean() {
	    FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	    AccessTokenFilter filter = new AccessTokenFilter(); 
	    filter.setAuthService(authService); 
	    filterRegistration.setFilter(filter);
	    filterRegistration.setEnabled(true); 
	    filterRegistration.addUrlPatterns(new String[] { "/*" });
	    filterRegistration.setOrder(1); 
	    return filterRegistration;
	}
	
	
	@Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();    
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Boolean.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Boolean.TYPE, ToStringSerializer.instance);
        
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8")); 
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
	 
	
	 
}
