package com.hotdms.proxy.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.http.HttpHost;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import com.alibaba.fastjson.JSONObject;

/**
 * 用于进行Https请求的HttpClient
 * 
 * @ClassName: SSLClient
 * @Description: TODO
 * @author Devin <xxx>
 * @date 2017年2月7日 下午1:42:07
 * 
 */
public class SSLClient extends DefaultHttpClient {
	
	private static final String HTTP = "http";
	
	private static final String HTTPS = "https";
	
	private static SSLConnectionSocketFactory sslsf = null;
	
	private static SSLContextBuilder builder = null;
	
	private static RequestConfig defaultRequestConfig = null;
	
	private static SocketConfig defaultSocketConfig = null;
	
	private static RequestConfig checkRequestConfig = null;
	
	private static SocketConfig checkSocketConfig = null;
	
	public static final String DEFAULT_USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
	
	public static void initialize() {
		try { 
			defaultRequestConfig = RequestConfig.custom()
					.setRedirectsEnabled(false)
					.setCookieSpec(CookieSpecs.STANDARD)
		            .setConnectionRequestTimeout(2 * 1000)
		            .setConnectTimeout(20 * 1000)
		            .setSocketTimeout(20 * 1000)
		            .build(); 
			
			defaultSocketConfig =  SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(20 * 1000)
                    .setTcpNoDelay(true).build();
			
			checkRequestConfig = RequestConfig.custom()
					.setRedirectsEnabled(false)
					.setCookieSpec(CookieSpecs.STANDARD)
		            .setConnectionRequestTimeout(2 * 1000)
		            .setConnectTimeout(5 * 1000)
		            .setSocketTimeout(5 * 1000)
		            .build(); 
			
			checkSocketConfig =  SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoLinger(1)
                    .setSoReuseAddress(true)
                    .setSoTimeout(5 * 1000)
                    .setTcpNoDelay(true).build();
			
			
			builder = new SSLContextBuilder();
			 
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			// sslsf = new SSLConnectionSocketFactory(builder.build());
			sslsf = new SSLConnectionSocketFactory(builder.build(),
					new String[] {  "TLSv1", "TLSv1.1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	 
	 private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
		    @Override
		    public boolean retryRequest(IOException exception,
			    	int executionCount, HttpContext context) {
		    	if(executionCount > 3) {
		    		return false;
		    	}
		    	if(exception instanceof UnknownHostException
		    			|| exception instanceof NoHttpResponseException
		    			|| exception instanceof HttpHostConnectException 
		      			|| exception instanceof SSLException
		      			|| exception instanceof SocketException ) { 
		    		System.out.println( "http thread " + Thread.currentThread().getId() 
		    				+ "exception " + exception.getClass().getName() + " retry " + executionCount + ":" + exception.getMessage());
		    		try {
		    			Thread.sleep(500);
		    		} catch(Exception e) {
		    			
		    		}    
		    		return true;
		    	} 
			    return false;
		    }
	 };
	 
	 private static HttpRequestRetryHandler noRetryHandler = new HttpRequestRetryHandler() {
		    @Override
		    public boolean retryRequest(IOException exception,
			    	int executionCount, HttpContext context) { 
			    return false;
		    }
	 };
	  
	 
	public static HttpClient createSSLClient(String ip, int port) {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultSocketConfig(defaultSocketConfig) 
				.setDefaultRequestConfig(defaultRequestConfig) 
                .setSSLSocketFactory(sslsf)
                .setRetryHandler(retryHandler)
                .setUserAgent(DEFAULT_USERAGENT)
                .setConnectionTimeToLive(15, TimeUnit.SECONDS)
                .setProxy(new HttpHost(ip, port))
                .setConnectionManagerShared(true)
                .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
                .build();
		return httpClient;
	}
	
	public static HttpClient createSSLClient() {	
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultSocketConfig(defaultSocketConfig) 
				.setDefaultRequestConfig(defaultRequestConfig) 
                .setSSLSocketFactory(sslsf)
                .setRetryHandler(retryHandler)
                .setUserAgent(DEFAULT_USERAGENT)
                .setConnectionTimeToLive(15, TimeUnit.SECONDS) 
                .setConnectionManagerShared(true)
                .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
                .build();
		return httpClient;
	}

	public static HttpClient createSSLProxyCheckClient(String ip, int port) {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultSocketConfig(checkSocketConfig) 
				.setDefaultRequestConfig(checkRequestConfig) 
	            .setSSLSocketFactory(sslsf)
	            .setRetryHandler(noRetryHandler)
	            .setUserAgent(DEFAULT_USERAGENT)
	            .setConnectionTimeToLive(5, TimeUnit.SECONDS)
	            .setProxy(new HttpHost(ip, port))
	            .setConnectionManagerShared(true)
	            .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
	            .build();
		return httpClient;
	}
	 

	public static void main(String[] arg) throws Exception {
		SSLClient.initialize();
		HttpSession session = new HttpSession(SSLClient.createSSLProxyCheckClient("8.219.97.248", 80));
		
		JSONObject s = session.sendGet("https://mail.163.com/register/asset-manifest.json", "UTF-8", "{}");
		System.out.println(s.getString("content"));
	}

}