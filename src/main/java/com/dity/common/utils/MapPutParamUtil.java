package com.dity.common.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MapPutParamUtil {

	private MapPutParamUtil() {
		
	}
	
	public static void put(Map<String, Object> map, String key, String value) {
		if(StringUtils.isNotBlank(value)) {
			map.put(key, value);
		}
	}
}
