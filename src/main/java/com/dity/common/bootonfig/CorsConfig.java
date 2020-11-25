package com.dity.common.bootonfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
				.allowCredentials(true).maxAge(3600);
	}

	@Bean
	public AuthenticationInterceptor authenticationInterceptor() {
	    return new AuthenticationInterceptor();
	}
	
	/**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = 
        			registry.addInterceptor(authenticationInterceptor());
        interceptorRegistration.addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns(
        		"/css/**",
        		"/js/**",
        		"/img/**",
        		"/tempFile/**",
        		"/fonts/**",
        		"/error",
        		"/dity/auth/**");
    }
}
