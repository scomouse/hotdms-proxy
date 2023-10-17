package com.hotdms.proxy.client;


import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;

import javax.net.ssl.SSLException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;  

// HTTP会话工具类
public class HttpSession {

	private static Logger logger = Logger.getLogger(HttpSession.class);

	private HttpClient httpClient;
 
	public HttpSession() {  
		this(SSLClient.createSSLClient());
	}
	
	public HttpSession(String ip , int port) {
		this(SSLClient.createSSLClient(ip , port)); 
	}
	
	public HttpSession(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
 
	public void close() {
		 if(httpClient instanceof CloseableHttpClient) {
			 	try {
			 		((CloseableHttpClient)httpClient).close();
			 	} catch(Exception e) {
			 		e.printStackTrace();
			 	}
		 }
	} 
	 

	public JSONObject sendGet(String url, String charset, String headers) {
		return sendGet(url, charset, JSON.parseObject(headers));
	}

	public JSONObject sendGet(String url, String charset, JSONObject headers) {
		JSONObject result = new JSONObject();
		long sendTime = System.currentTimeMillis();
		try {
			HttpGet httpGet = new HttpGet(url);
			if (headers != null) {
				Iterator keys = headers.keySet().iterator();
				while (keys.hasNext()) {
					String name = (String) keys.next();
					httpGet.setHeader(name, headers.getString(name));
				}
			} 
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				result.put("code", statusCode);
				if(statusCode == 302) {
					Header header = response.getLastHeader("Location");
					result.put("redirect", header.getValue());
				}
				else {
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						if (charset != null) {
							String content = EntityUtils.toString(resEntity, charset);
							result.put("content", content);
						} else {
							InputStream input = resEntity.getContent();
							byte[] b = new byte[1024];
							int cnt = -1;
							while ((cnt = input.read(b)) != -1) {
							}
							input.close(); 
						}
					}
				}
			}
		} catch (HttpHostConnectException hce) {
			throw new  ClientRuntimeException(ClientRuntimeException.TYPE_HOST_CONNECT, sendTime, "代理主机连接异常", hce);
		} catch (SocketTimeoutException ste) {
			throw new  ClientRuntimeException(ClientRuntimeException.TYPE_SOCKET_TIMEOUT, sendTime, "代理主机连接超时", ste);
		} catch (NoHttpResponseException nre) {
			throw new  ClientRuntimeException(ClientRuntimeException.TYPE_HOST_RESPONSE, sendTime, "代理主机没有响应", nre);
		} catch (ConnectTimeoutException cte) {
			throw new  ClientRuntimeException(ClientRuntimeException.TYPE_CONNECT_TIMEOUT, sendTime, "代理主机连接超时", cte);
		} catch (SocketException sre) {  
			throw new  ClientRuntimeException(ClientRuntimeException.TYPE_SOCKET_READ, sendTime, "网络请求端口异常, 等待时间：" + (System.currentTimeMillis() - sendTime) + "毫秒", sre);
		} catch (SSLException sse) {
			throw new ClientRuntimeException(ClientRuntimeException.TYPE_HOST_SSL, sendTime, "SSL协议异常, 等待时间：" + (System.currentTimeMillis() - sendTime) + "毫秒", sse);
		} catch (UnknownHostException uhe) {
			throw new ClientRuntimeException(ClientRuntimeException.TYPE_HOST_DNS, sendTime, "代理主机DNS异常", uhe);
		} catch (ClientRuntimeException ipre) {
			throw ipre; 
		} catch (Exception ex) {
			logger.error("http请求错误", ex);
			ex.printStackTrace();
		}
		return result;
	}

}
