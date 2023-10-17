package com.hotdms.proxy.web;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject; 

public class WebUtils {
	
	public static final String CODE_SUCCESS = "SUCCESS";
	
	public static final String CODE_FAILURE = "FAILURE"; 
	
	public static final String LOGIN_ATTRIBUTE = "login"; 
	
	public static final String ACCESS_TOKEN = "ACCESS-TOKEN";
	
	public static final String ACCESS_TOKEN2 = "ACCESS_TOKEN";
	
	public static final String ACCESS_TOKEN_IN_COOKIE = "access_token"; 
	 
	public static final String ERROR_RESULT = "errorresult";
	
	public static final long TOKEN_DURATION = 1000 * 60 * 60 * 24;
	
	public static final String STATUS_OK  = "0";
	
	public static final String STATUS_DISABLED  = "1";
	
	public static final String STATUS_ERROR  = "1";
	
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
	protected static SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
	 
	protected static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
	 
	
	public static String formatNumber(int index, int count) {
		String s = ("000000" + index);
		return s.substring(s.length() - count);
	}
	
	public static Date parseDate(String date) throws Exception {
		return dateFormat3.parse(date);
	}
	
	public static Date parseFullDate(String date)  {
		try {
			return dateFormat.parse(date);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String formatFileDate(Date date) {
		return dateFormat2.format(date);
	}
	
	public static String formatDate(Date date) {
		if(date == null) return null;
		return dateFormat.format(date);
	}
	
	public static String formatYMDDate(Date date) {
		return dateFormat3.format(date);
	}
	
	public static String formatDate(long time) {
		return  (time == 0L) ? "" : dateFormat.format(new Date(time));
	}
	  
	public static String encodeURIComponent(String str) {
		try {
			str = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String replaceEmpty(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\0|\ufeff|\b|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	public static boolean isNotBlank(String value) {
		return StringUtils.isNotBlank(value);
	}
	
	public static boolean isBlank(String value) {
		return StringUtils.isBlank(value);
	}
	
	public static boolean isDigits(String value) {
		return NumberUtils.isDigits(value);
	}
	
	public static boolean equals(String str1, String str2) {
		 if( str1 == null || str1.length() == 0) {
			 return str2 == null || str2.length() == 0;
		 } 
		 return str1.equals(str2);
	}
	
	public static boolean equals(Integer int1, Integer int2) {
		if(int1 == null || int1 == 0)  return int2 == null || int2 ==0;
		if(int2 == null || int2 == 0)  return int1 == null || int1 ==0;
		return int1.intValue() == int2.intValue();
	} 
	
	public static String getRequestURI(HttpServletRequest request) {
		String url = request.getRequestURI();
		if ( url != null && url.indexOf("//") > -1) {
			url = url.replaceAll("//+", "/");
		}
		String proxyPath = request.getHeader("X-Loc-Base");
		if (proxyPath != null && proxyPath.length() > 0 && url.startsWith(proxyPath)) {
			return url.substring(proxyPath.length());
		}
		String contextPath = request.getContextPath();
		if (contextPath != null && contextPath.length() > 0) {
			return url.substring(contextPath.length());
		}
		return url;
	}
	
	public static String getContextPath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		if (StringUtils.isBlank(contextPath)) {
			return "/";
		}
		if (contextPath.charAt(0) != '/') {
			contextPath = '/' + contextPath;
		}
		if (contextPath.charAt(contextPath.length() - 1) != '/')
			contextPath += '/';

		return contextPath;
	}
	 
	public static int findIndex(String texts, String split, String str) {
		if(StringUtils.isBlank(str) || StringUtils.isBlank(texts)) {
			return -1;
		}
		int index = -1;
		String[] array = texts.split(split);	
		for(int i=0; i< array.length; i++) {
			if(array[i].contains("*")) {
				index = matchIndexOf(array[i], str);
			} else {
				index = str.indexOf(array[i]);
			}
			if(index > -1) return index;
		}
		return -1;
	}
	
	private static int matchIndexOf(String like, String str) {
		String[] array = like.split("\\*");
		int start = -1, index = -1;
		int pos = 0;
		for(int i=0; i< array.length; i++) {
			index = str.indexOf(array[i], pos);
			if(index ==  -1) return -1;
			if(i == 0) start = index;
			pos = index + array[i].length();
		}
		return  start;
	}

	public static boolean inArray(String texts, String split, String str) {
		if(StringUtils.isBlank(str) || StringUtils.isBlank(texts)) {
			return false;
		}
		String[] array = texts.split(split);	
		for(int i=0; i< array.length; i++) {
			if(str.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean inArray(String[] array, String str) {
		if(StringUtils.isBlank(str)) {
			return false;
		}
		for(int i=0; i< array.length; i++) {
			if(str.equals(array[i])) {
				return true;
			}
		}
		return false;
	} 
	 
	public static int indexOf(String[] array, String str) {
		if(StringUtils.isBlank(str)) {
			return -1;
		}
		for(int i=0; i< array.length; i++) {
			if(array[i].equals(str) || str.contains(array[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public static int indexOf(char[] array, char str) { 
		for(int i=0; i< array.length; i++) {
			if(array[i] == str) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getIndex(String[] array, String str) {
		if(StringUtils.isBlank(str)) {
			return -1;
		}
		for(int i=0; i< array.length; i++) {
			if(array[i].equals(str)) {
				return i;
			}
		}
		return -1;
	}
	
	public static String join(String[] array, String str) {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i< array.length; i++) {
			if(i>0) buffer.append(str);
			buffer.append(array[i]);
		}
		return buffer.toString();
	}
	 
	 
	public static boolean inArray(List<String> array, String str) {
		if(StringUtils.isBlank(str)) {
			return false;
		}
		for(int i=0; i< array.size(); i++) {
			if(array.get(i).equals(str)) {
				return true;
			}
		}
		return false;
	}
	 
	
	public static HashMap<String,String> toHashMap(String text) {
		HashMap<String,String> map = new HashMap<String,String>();
		String[] data = text.split(";");
		for(int i=0; i< data.length; i++) {
			String[] item = data[i].split("=");
			map.put(item[0], item[1]);
		}
		return map;
	}
	
	public static String toJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}
	
	public static List<String> toList(String str, String split) {
		return toList(str, split, true);
	}
	
	public static List<Long> toLongList(String str, String split) {
		return toLongList(str, split, true);
	} 
	 
	public static List<Long> toLongList(String str, String split, boolean removeDuplicate) {
		ArrayList<Long> list = new ArrayList<Long>();
		if(!StringUtils.isEmpty(str)) {
			String[] array = str.split(split); 
			for(int i= 0; i< array.length; i++) { 
				Long value = Long.valueOf(array[i]);
				if(!removeDuplicate || !list.contains(value)) 
				list.add(value); 
			}
		} 
		return list;
	}
	
	public static List<String> toList(String str, String split, boolean removeDuplicate) {
		ArrayList<String> list = new ArrayList<String>();
		if(!StringUtils.isEmpty(str)) {
			String[] array = str.split(split); 
			for(int i= 0; i< array.length; i++) { 
				if(!removeDuplicate || !list.contains(array[i]))
				list.add(array[i]); 
			}
		} 
		return list;
	}
	
	public static String toString(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(int i =0; i< data.length; i++) {
			if(i % 10 == 0) buffer.append("\n");
			buffer.append( (int) ( data[i] & 0xff) ).append(",");
			 
		}
		buffer.setLength(buffer.length() - 1);
		buffer.append("]");
		return buffer.toString();
	}
	
	public static String toUrlParams(JSONObject params) {
		StringBuffer buffer = new StringBuffer();
		Iterator<String> iteraor = params.keySet().iterator();
		boolean first = true;
		while(iteraor.hasNext()) {
			String key = iteraor.next();
			String value = params.getString(key);
			if(!first) buffer.append("&");
			buffer.append(key).append("=").append(StringUtils.isEmpty(value)? "" : encodeURIComponent(value));
			first = false;
		}
		return buffer.toString();
	}
	public static Object getAttribute(HttpServletRequest request, String attributeName) {
		return request.getSession().getAttribute(attributeName);
	}
	
	public static void setAttribute(HttpServletRequest request, String attributeName, Object attributeValue) {
		request.getSession().setAttribute(attributeName, attributeValue);
	}
	
	public static void removeAttribute(HttpServletRequest request, String attributeName) {
		request.getSession().removeAttribute(attributeName);
	}
	
	public static boolean isLogin(HttpServletRequest request) {
		return getAttribute(request, LOGIN_ATTRIBUTE) != null;
	}
	
	public static void setLoginObj(HttpServletRequest request, Object login) {
		setAttribute(request, LOGIN_ATTRIBUTE, login);
	}
	
	public static Object getLoginObj(HttpServletRequest request) {
		return getAttribute(request, LOGIN_ATTRIBUTE);
	}
	
	public static void removeLoginObj(HttpServletRequest request) {
		removeAttribute(request, LOGIN_ATTRIBUTE);
	}
	
	public static String getAccessToken(HttpServletRequest request, boolean saveInCookie) {
		if(saveInCookie) {
			Cookie cookie = getCookie(request, ACCESS_TOKEN_IN_COOKIE);
			if(cookie != null) {
				return cookie.getValue();
			}
		}
		String token = getHeader(request, ACCESS_TOKEN2);
		if(!StringUtils.isEmpty(token)) {
			return token;
		}
		return getHeader(request, ACCESS_TOKEN);
	}
	
	public static void setAccessToken(HttpServletResponse response, String token, long duration, boolean saveInCookie) {
		setHeader(response, ACCESS_TOKEN, token);
		if(saveInCookie) {
			addCookie(response, ACCESS_TOKEN_IN_COOKIE, token, (int)duration);
		}
	} 
	
	public static void setErrorResult(HttpServletRequest request, Result<Object> result) {
		setAttribute(request, ERROR_RESULT, result);
	}
	
	public static Result<Object> getErrorResult(HttpServletRequest request) {
		return (Result<Object>)getAttribute(request, ERROR_RESULT);
	}
	 
	public static String getHeader(HttpServletRequest request, String headerName) {
		return request.getHeader(headerName);
	}
	
	public static void setHeader(HttpServletResponse response, String headerName, String headerValue) {
		response.setHeader(headerName, headerValue);
	}
	
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int expiry) {
		Cookie cookie = new Cookie(cookieName, cookieValue);   
		cookie.setMaxAge(expiry);
        response.addCookie(cookie);
	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}
	
	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent"); 
		if( StringUtils.isEmpty(userAgent) ) {
			userAgent = "UNKNOWN"; 
		}
		return userAgent;
	}
	
	public static String getRemoteIp(HttpServletRequest request) {
		String remoteIp = request.getHeader("X-Real-IP");
		if(!StringUtils.isEmpty(remoteIp) ) {
			return remoteIp;
		}
		remoteIp = request.getRemoteHost(); 
		if( StringUtils.isEmpty(remoteIp) ) {
			remoteIp = "UNKNOWN"; 
		}
		return remoteIp;
	}
	
	private static final String RANDOM_KEYS = "varfunctio0125634789bdegjhklmpqswxyz";
	
	private static final String RANDOM_NUM_KEYS = "2170459863501256347893248160759163724";
	
	// 生成随机串，长度为count默认21位
	public static String getRandomString(int count) { 
		StringBuffer buffer = new StringBuffer(); 
		for (int i=0; i< count; i++ )
			buffer.append(RANDOM_KEYS.charAt((int)( 36 * Math.random())));
        return buffer.toString();
	}
	
	public static String getRandomNumberString(int count) { 
		StringBuffer buffer = new StringBuffer(); 
		for (int i=0; i< count; i++ )
			buffer.append(RANDOM_NUM_KEYS.charAt((int)( 36 * Math.random())));
        return buffer.toString();
	}
	 
	public static String findMatch(String text) {
		Matcher matcher = Pattern.compile("\\{\\d+-\\d+\\}").matcher(text);
		 if(matcher.find()) {
			   return matcher.group(); 
		 }
		 return null;
	}
	
	public static boolean endsWith(String str, String[] texts) {
		for(String text : texts) {
			if(str.endsWith(text)) return true;
		}
		return false;
	}
	
}
