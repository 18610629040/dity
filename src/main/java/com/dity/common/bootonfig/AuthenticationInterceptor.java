package com.dity.common.bootonfig;

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
		HttpSession session = httpServletRequest.getSession();
		String token = (String) session.getAttribute("token");
		// 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
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
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    httpServletResponse.sendRedirect("/dity/auth/notLogin");
                    return false;
                }
                // 获取 token 中的 userId
                String userId = "";
                try {
                	userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                	httpServletResponse.sendRedirect("/dity/auth/notLogin");
                    return false;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("USER_ID", userId);
                List<Map<String, Object>> user = (List<Map<String, Object>>) authService.getUserInfo(map);
                if (user.isEmpty()) {
                	httpServletResponse.sendRedirect("/dity/auth/notLogin");
                	return false;
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256((String)user.get(0).get("PASS"))).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                	httpServletResponse.sendRedirect("/dity/auth/notLogin");
                	return false;
                }
                return true;
            }
        }
        return true;
    }
	
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    	
    }
    
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    	
    }
}
