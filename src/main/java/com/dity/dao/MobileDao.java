package com.dity.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("mobileDao")
public class MobileDao {

	@Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
	
	public List<Map<String,Object>> getUserByNo(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.MobileMapper.getUserByNo", map);
	}

	public List<Map<String, Object>> qryRottnImage(Map<String, Object> map) {
		return sqlSessionTemplate
		        .selectList("com.dity.dao.MobileMapper.qryRottnImage", map);
	}
}
