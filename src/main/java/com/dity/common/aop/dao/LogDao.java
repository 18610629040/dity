package com.dity.common.aop.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao {
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int insertLog(Map<String,Object> param) {
		return sqlSessionTemplate.insert("com.dity.common.aop.dao.LogDaoMapper.insertLog",param);
	}
}
