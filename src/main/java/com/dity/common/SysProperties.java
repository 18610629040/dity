package com.dity.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SysProperties {
	@Value("${accessFile.resourceHandler}")
    private String resourceHandler; //匹配url 中的资源映射
    
    @Value("${accessFile.location}")
    private String location; //上传文件保存的本地目录

	public String getResourceHandler() {
		return resourceHandler;
	}

	public String getLocation() {
		return location;
	}
    
}
