package com.dity.common.bootonfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DityWebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = 
        			registry.addInterceptor(new AuthenticationInterceptor());
        interceptorRegistration.excludePathPatterns(
        		"/css/**",
        		"/js/**",
        		"/img/**",
        		"/fonts/**",
        		"/index.html",
        		"/dity/auth/login");
        interceptorRegistration.addPathPatterns("/**");
    }

}