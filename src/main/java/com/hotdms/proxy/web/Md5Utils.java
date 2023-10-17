package com.hotdms.proxy.web;

import java.security.MessageDigest;

public class Md5Utils {
	
	public static String encrypt32(String str) {
		int i = 0;
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(str.getBytes("UTF-8"));
			StringBuffer stringBuffer = new StringBuffer();
			while (i < digest.length) {
				int i3 = digest[i] & 255;
				if (i3 < 16) {
					stringBuffer.append("0");
				}
				stringBuffer.append(Integer.toHexString(i3));
				i++;
			}
			return stringBuffer.toString();
		} catch (Throwable th) {
			return "";
		}
	}

}
