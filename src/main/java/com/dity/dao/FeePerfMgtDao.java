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
	
}
