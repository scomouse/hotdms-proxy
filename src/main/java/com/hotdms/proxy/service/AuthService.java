package com.hotdms.proxy.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.hotdms.proxy.web.Md5Utils;
import com.hotdms.proxy.web.WebUtils;

@Service
@ConfigurationProperties(prefix = "hotdms.proxy.admin")
public class AuthService {
	
	private String userid;
	
	private String password;
	
	private String salt;
	
	public String doLogin(String userid, String password) {
		boolean canLogin =  this.userid.equals(userid) && this.password.equals(password);
		if(canLogin) {
			salt = WebUtils.getRandomString(6);
			return Md5Utils.encrypt32(userid + password + salt);
		}
		return null;
	}
	
	public boolean checkToken(String  token) {
		return token != null && salt!= null && token.equals(Md5Utils.encrypt32(userid + password + salt));
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

 
	public void expireLogin() {
		salt = null;
	}
}
