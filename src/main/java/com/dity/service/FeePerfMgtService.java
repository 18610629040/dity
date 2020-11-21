package com.dity.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dity.dao.FeePerfMgtDao;

@Service("feePerfService")
public class FeePerfMgtService {
	@Autowired
	FeePerfMgtDao feePerfMgtDao; 
	
	public List<Object> srchFeePerfData(Map<String, Object> map) {
		return feePerfMgtDao.srchFeePerfData(map);
	}
}
