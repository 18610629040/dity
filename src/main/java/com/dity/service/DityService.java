package com.dity.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dity.dao.DityDao;

@Service("dityService")
public class DityService {
	@Autowired
	DityDao dityDao; 
	
	public List<Object> qryUserList(Map<String, Object> map) {
		return dityDao.qryUserList(map);
	}
	
	public List<Map<String,Object>> getUserByNo(Map<String, Object> map) {
		return dityDao.getUserByNo(map);
	}

	public int addUser(Map<String, Object> map) {
		return dityDao.addUser(map);
	}

	public int editUser(Map<String, Object> map) {
		return dityDao.editUser(map);
	}

	public int delUser(Map<String, Object> map) {
		return dityDao.delUser(map);
	}
	
	
	public int addRottn(Map<String, Object> map) {
		return dityDao.addRottn(map);
	}

	public List<Map<String, Object>> qryRottnChrt(Map<String, Object> map) {
		return dityDao.qryRottnChrt(map);
	}
	
	public int delItton(Map<String, Object> map) {
		return dityDao.delItton(map);
	}
	
	public List<Map<String, Object>> qryRottnChrtById(Map<String, Object> map) {
		return dityDao.qryRottnChrtById(map);
	}
	
	public int editIttonOrder(Map<String, Object> map) {
		return dityDao.editIttonOrder(map);
	}
}
