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
import com.dity.service.AuthService;

public class AuthenticationInterceptor implements HandlerInterceptor{
	
	@Autowired
	AuthService authService;
	
	@SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
		System.out.println(httpServletRequest.getRequestURI());
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
        //检查是否需要校验token  	方法所在类有注解、或方法上注解
        if (method.getDeclaringClass().isAnnotationPresent(UserLoginToken.class) || 
        		method.isAnnotationPresent(UserLoginToken.class)) {
        	UserLoginToken userLoginToken = null;
        	if(method.getDeclaringClass().isAnnotationPresent(UserLoginToken.class)) {
        		userLoginToken = method.getDeclaringClass().getAnnotation(UserLoginToken.class);
        	}else {
        		userLoginToken = method.getAnnotation(UserLoginToken.class);
        	}
            if (userLoginToken.required()) {
            	// 执行认证
            	HttpSession session = httpServletRequest.getSession();
        		String token = (String) session.getAttribute("token");
                if (token == null) {
                	redirectUrl(httpServletRequest,httpServletResponse);
                    return false;
                }
                // 验证 token
                try {
                	// 获取 token 中的 userNo
                	String userNo = JWT.decode(token).getAudience().get(0);
                	Map<String, Object> map = new HashMap<>();
                	map.put("USER_NO", userNo);
                    List<Map<String, Object>> list = (List<Map<String, Object>>) authService.getUserInfo(map);
                    Map<String, Object> user = list.get(0);
                	JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256((String)user.get("PASS"))).build();
                    jwtVerifier.verify(token);
                } catch (Exception e) {
                	//token验证失败，或token已过期，TokenService中设置有效时间，或用户被删了，改了密码。
                	redirectUrl(httpServletRequest,httpServletResponse);
                	return false;
                }
            }
        }
        return true;
    }
	
	public void redirectUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException{
		boolean flag = httpServletRequest.getRequestURI().indexOf("mobile")>=0;
		if(flag) {
			System.out.println(httpServletRequest.getRequestURI() + "false"); 
			httpServletResponse.sendRedirect("/dity/mobile/auth/notLogin");//手机端的
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
