package com.dity.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dity.dao.MobileDao;

@Service("mobileService")
public class MobileService {
	@Autowired
	MobileDao dao; 
	
	public List<Map<String,Object>> getUserByNo(Map<String, Object> map) {
		return dao.getUserByNo(map);
	}

	public List<Map<String,Object>> qryRottnImage(Map<String, Object> map) {
		return dao.qryRottnImage(map);
	}
}
