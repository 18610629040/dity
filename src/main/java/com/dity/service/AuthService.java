package com.dity.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dity.dao.AuthDao;

@Service("authService")
public class AuthService {
	@Autowired
	AuthDao authDao; 
	public Object getUserInfo(Map<String, Object> map) {
		return authDao.getUserInfo(map);
	}
}
