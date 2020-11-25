package com.dity.common.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {

	private SessionUtil() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static String getUserNo() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("userInfo");
		if(user!=null) {
			return user.get("USER_NO").toString();
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getUser() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("userInfo");
		return user;
	}
}
