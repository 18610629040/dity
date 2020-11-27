package com.dity.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("feePerfMgtDao")
public class FeePerfMgtDao {

	@Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

	public List<Object> srchFeePerfData(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.FeePerfMgtDaoMapper.srchFeePerfData", map);
	}

	public int addFeePerfData(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.FeePerfMgtDaoMapper.addFeePerfData", map);
	}

	public int modfyFeePerfData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.FeePerfMgtDaoMapper.modfyFeePerfData", map);
	}

	public int delFeePerfData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.FeePerfMgtDaoMapper.delFeePerfData", map);
	}

	public int addPrdtLbData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .insert("com.dity.dao.FeePerfMgtDaoMapper.addPrdtLbData", map);
	}

	public int modfyPrdtLbData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.FeePerfMgtDaoMapper.modfyPrdtLbData", map);
	}

	public int delPrdtLbData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.FeePerfMgtDaoMapper.delPrdtLbData", map);
	}

	public int addInfoTsData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .insert("com.dity.dao.FeePerfMgtDaoMapper.addInfoTsData", map);
	}

	public int modfyInfoTsData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.FeePerfMgtDaoMapper.modfyInfoTsData", map);
	}

	public int delInfoTsData(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.FeePerfMgtDaoMapper.delInfoTsData", map);
	}

	public List<Object> srchInfoTsData(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.FeePerfMgtDaoMapper.srchInfoTsData", map);
	}

	public List<Object> srchPrdtLbData(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.FeePerfMgtDaoMapper.srchPrdtLbData", map);
	}

	public List<Object> srchPdOrderData(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.FeePerfMgtDaoMapper.srchPdOrderData", map);
	}
	
}
