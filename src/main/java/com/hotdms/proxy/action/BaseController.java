package com.hotdms.proxy.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.LoginForm;
import com.hotdms.proxy.service.AuthService;
import com.hotdms.proxy.web.Result;
import com.hotdms.proxy.web.WebUtils; 

@Controller
@RequestMapping("/base")
public class BaseController {
	 
	@Autowired
	private AuthService authService; 
	
	@ResponseBody
	@RequestMapping({ "/login" })
	public Result<Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm form) { 
		try {
		String token = authService.doLogin(form.getUserid(), form.getPassword());
		if( token!= null) {
			WebUtils.setLoginObj(request, token);
			WebUtils.setAccessToken(response, token, WebUtils.TOKEN_DURATION, true);
		}
		JSONObject obj = new JSONObject(); 
		obj.put("token", token);
		return Result.success("登录成功", obj);
		} catch(Exception e) {
			e.printStackTrace();
			return Result.failure(null, e.getMessage());
		}
	}
	 
	@ResponseBody
	@RequestMapping({ "/checkToken" })
	public Result<Object> checkToken(HttpServletRequest request, HttpServletResponse response) {
		String token = WebUtils.getAccessToken(request, true);
		boolean valid = authService.checkToken(token);
		if(valid) {
			return Result.success("valid");
		}
		return Result.failure("invalid");
	}
	
	@ResponseBody
	@RequestMapping({ "/logout" })
	public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.expireLogin();
		WebUtils.removeLoginObj(request);
		
		return Result.success("退出登录成功");
	}

	@ResponseBody
	@RequestMapping({ "/error" })
	public Result<Object> error(HttpServletRequest request, HttpServletResponse response) {
		Result<Object> result = WebUtils.getErrorResult(request);
		if (result == null) {
			result = Result.failure("unknown error");
		}
		return result;
	}
}
