package com.dity.common.aop.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dity.common.aop.dao.LogDao;



@Service
public class LogService {
	
	@Autowired
	LogDao logDao;
	
	public int insertLog(Map<String,Object> param) {
		return logDao.insertLog(param);
	}

}
