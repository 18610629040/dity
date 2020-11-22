package com.dity.common.bootonfig;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dity.service.AuthService;

public class AuthenticationInterceptor implements HandlerInterceptor{
	
	@Autowired
	AuthService authService;
	
	@SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
		System.out.println(httpServletRequest.getRequestURI());
		HttpSession session = httpServletRequest.getSession();
		String token = (String) session.getAttribute("token");
		// 执行认证
        if (token == null) {
            redirectUrl(httpServletRequest,httpServletResponse);
            return false;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 获取 token 中的 userNo
        String userNo = "";
        try {
        	userNo = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
        	redirectUrl(httpServletRequest,httpServletResponse);
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("USER_NO", userNo);
        List<Map<String, Object>> user = (List<Map<String, Object>>) authService.getUserInfo(map);
        if (user.isEmpty()) {
        	redirectUrl(httpServletRequest,httpServletResponse);
        	return false;
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256((String)user.get(0).get("PASS"))).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
        	redirectUrl(httpServletRequest,httpServletResponse);
        	return false;
        }
        return true;
    }
	
	public void redirectUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException{
		boolean flag = httpServletRequest.getRequestURI().indexOf("mobile")>=0;
		if(flag) {
			httpServletResponse.sendRedirect("/dity/auth/notLogin");//手机端的
		}else {
			httpServletResponse.sendRedirect("/dity/auth/gotoLogin");//pc
		}
	}
	
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    	
    }
    
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    	
    }
}
