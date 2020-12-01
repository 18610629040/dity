package com.dity.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("dityDao")
public class DityDao {

	@Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

	public List<Object> qryUserList(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryUserList", map);
	}
	
	public List<Map<String,Object>> getUserByNo(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.getUserByNo", map);
	}

	public int addUser(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addUser", map);
	}

	public int editUser(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editUser", map);
	}

	public int delUser(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.DityDaoMapper.delUser", map);
	}
	
	public int addRottn(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addRottn", map);
	}

	public List<Map<String, Object>> qryRottnChrt(Map<String, Object> map) {
		return sqlSessionTemplate
		        .selectList("com.dity.dao.DityDaoMapper.qryRottnChrt", map);
	}
	
	public int delItton(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.DityDaoMapper.delItton", map);
	}
	
	public int editIttonOrder(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editIttonOrder", map);
	}
	
	public List<Map<String, Object>> qryRottnChrtById(Map<String, Object> map) {
		return sqlSessionTemplate
		        .selectList("com.dity.dao.DityDaoMapper.qryRottnChrtById", map);
	}
	
	
	public List<Map<String,Object>> qryGoodsList(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryGoodsList", map);
	}
	
	public int addGoods(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addGoods", map);
	}
	
	public int editGoods(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editGoods", map);
	}
	
	public int setGoodsStatus(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setGoodsStatus", map);
	}
	
	public int delGoods(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.DityDaoMapper.delGoods", map);
	}
	
	
	public List<Map<String,Object>> qryGoodsMsList(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryGoodsMsList", map);
	}
	
	public int addGoodsMs(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addGoodsMs", map);
	}
	
	public int editGoodsMs(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editGoodsMs", map);
	}
	
	public int setGoodsMsStatus(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setGoodsMsStatus", map);
	}
	
	public int delGoodsMs(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.DityDaoMapper.delGoodsMs", map);
	}
}
