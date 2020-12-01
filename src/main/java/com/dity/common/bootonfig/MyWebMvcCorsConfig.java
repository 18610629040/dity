package com.dity.common.bootonfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dity.common.SysProperties;

@Configuration
public class MyWebMvcCorsConfig implements WebMvcConfigurer{
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
        		"/*.jpg",
        		"/css/**",
        		"/js/**",
        		"/img/**",
        		"/tempFile/**",
        		"/fonts/**",
        		"/error",
        		"/dity/auth/**",
        		"/dity/mobile/auth/**");
    }
    
    @Autowired
    SysProperties sysProperties;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //匹配到路径,映射到本地文件夹上o 
        registry.addResourceHandler(sysProperties.getResourceHandler())
        	.addResourceLocations("file:" + sysProperties.getLocation());
    }
}
