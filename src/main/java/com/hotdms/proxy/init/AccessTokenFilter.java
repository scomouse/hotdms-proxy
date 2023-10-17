package com.hotdms.proxy.init;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hotdms.proxy.service.AuthService;
import com.hotdms.proxy.web.Result;
import com.hotdms.proxy.web.WebUtils;

public class AccessTokenFilter extends OncePerRequestFilter {
	
	// 免登录或不需要登录的服务地址
	private static final String[] OPEN_URL_API = { "/favicon.ico", "/base/login", "/base/checkToken", "/proxy/fetch/last", "/proxy/fetch/random"}; 
	 
	private static final String[] STATIC_RESOURCES = { ".js", ".css", ".html", ".ico", ".jpg", ".png", ".svg" };
	private AuthService authService;
	
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Result<Object> result = null;
		// 已经登陆了，或者不需要登录，则直接通过
		String requestUri = WebUtils.getRequestURI(request);
		if(WebUtils.endsWith(requestUri, STATIC_RESOURCES)) {
			filterChain.doFilter(request, response);
			return;
		}
		if(WebUtils.isLogin(request) || 
				WebUtils.inArray(OPEN_URL_API, requestUri)) {
			filterChain.doFilter(request, response);
			return;
		}  
		// 没有登陆，且存在access_token参数
		String tokenid = WebUtils.getAccessToken(request, true);
		if(StringUtils.isEmpty(tokenid)) {
			WebUtils.setErrorResult(request, Result.failure("TOKEN NOT FOUND"));
			request.setAttribute("request_origin_uri", requestUri);
			request.getRequestDispatcher("/base/error").forward(request, response);	
			return ;
		}
		boolean isValidToken = authService.checkToken(tokenid); 
		if(!isValidToken) { 
			WebUtils.setErrorResult(request, Result.failure("TOKEN IS NOT VALID"));
			request.setAttribute("request_origin_uri", requestUri);
			request.getRequestDispatcher("/base/error").forward(request, response);
			return ;
		} 
		filterChain.doFilter(request, response); 
	} 
	
	 
 
}
