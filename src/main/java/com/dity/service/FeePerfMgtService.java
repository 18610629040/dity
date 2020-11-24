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

	public int addFeePerfData(Map<String, Object> map) {
		return feePerfMgtDao.addFeePerfData(map);
	}

	public int modfyFeePerfData(Map<String, Object> map) {
		return feePerfMgtDao.modfyFeePerfData(map);
	}

	public int delFeePerfData(Map<String, Object> map) {
		return feePerfMgtDao.delFeePerfData(map);
	}

	public int addPrdtLbData(Map<String, Object> map) {
		return feePerfMgtDao.addPrdtLbData(map);
	}

	public int modfyPrdtLbData(Map<String, Object> map) {
		return feePerfMgtDao.modfyPrdtLbData(map);
	}

	public int delPrdtLbData(Map<String, Object> map) {
		return feePerfMgtDao.delPrdtLbData(map);
	}

	public int addInfoTsData(Map<String, Object> map) {
		return feePerfMgtDao.addInfoTsData(map);
	}

	public int modfyInfoTsData(Map<String, Object> map) {
		return feePerfMgtDao.modfyInfoTsData(map);
	}

	public int delInfoTsData(Map<String, Object> map) {
		return feePerfMgtDao.delInfoTsData(map);
	}

	public List<Object> srchInfoTsData(Map<String, Object> map) {
		return feePerfMgtDao.srchInfoTsData(map);
	}

	public List<Object> srchPrdtLbData(Map<String, Object> map) {
		return feePerfMgtDao.srchPrdtLbData(map);
	}
}
