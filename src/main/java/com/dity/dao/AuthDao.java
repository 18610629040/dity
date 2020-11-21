package com.dity.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("authDao")
public class AuthDao {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Map<String, Object>> getUserInfo(Map<String, Object> map) {
        return sqlSessionTemplate
                .selectList("com.dity.dao.AuthDaoMapper.getUserInfo", map);
    }
    
    public int saveUser(Map<String, Object> map) {
        return sqlSessionTemplate
                .insert("com.dity.dao.AuthDaoMapper.saveUser", map);
    }
}
