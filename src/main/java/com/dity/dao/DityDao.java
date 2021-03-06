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
	
	
	public int qryGoodsListCt(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectOne("com.dity.dao.DityDaoMapper.qryGoodsListCt", map);
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
	
	public List<Map<String,Object>> qryUserDz(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryUserDz", map);
	}
	
	public int addUserDz(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addUserDz", map);
	}
	
	public int editUserDz(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editUserDz", map);
	}
	
	public int setUserDzMr(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setUserDzMr", map);
	}
	
	public void setUserDzMr2(Map<String, Object> map) {
		sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setUserDzMr2", map);
	}
	
	public int delUserDz(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.DityDaoMapper.delUserDz", map);
	}
	
	public List<Map<String,Object>> qryOrder(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryOrder", map);
	}
	
	public List<Map<String,Object>> qryFsOrder(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryFsOrder", map);
	}
	
	public int addOrder(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addOrder", map);
	}
	
	public int setOrder(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setOrder", map);
	}
	
	public int setGoodPrice(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setGoodPrice", map);
	}
	
	public int delOrder(Map<String, Object> map) {
		return sqlSessionTemplate
		        .delete("com.dity.dao.DityDaoMapper.delOrder", map);
	}
	
	public int editGoodsYsc(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editGoodsYsc", map);
	}
	
	public int editGoodsOwnAcnt(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editGoodsOwnAcnt", map);
	}
	
	public int setOrderExpress(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setOrderExpress", map);
	}
	
	public int editGoodsMsPdStatus(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editGoodsMsPdStatus", map);
	}
	
	public int editGoodsPdStatus(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.editGoodsPdStatus", map);
	}
	
	public List<Map<String,Object>> qryFsUser(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryFsUser", map);
	}
	
	public List<Map<String,Object>> qryMyOrder(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryMyOrder", map);
	}
	
	public int setUserWtsk(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.setUserWtsk", map);
	}
	
	public int clearUserWtsk(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.clearUserWtsk", map);
	}
	
	public List<Map<String,Object>> qryWtSkInfo(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryWtSkInfo", map);
	}
	
	public List<Map<String,Object>> qryOrderSkInfo(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.qryOrderSkInfo", map);
	}
	
	public List<Map<String,Object>> checkOrder(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.checkOrder", map);
	}
	
	public int clearUserSk(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.clearUserSk", map);
	}
	
	public List<Map<String,Object>> getYjInfo(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.getYjInfo", map);
	}
	public List<Map<String,Object>> getYjList(Map<String, Object> map) {
		return sqlSessionTemplate
                .selectList("com.dity.dao.DityDaoMapper.getYjList", map);
	}
	public int addYJ(Map<String, Object> map) {
		return sqlSessionTemplate
        .insert("com.dity.dao.DityDaoMapper.addYJ", map);
	}
	public int updateYjStatus(Map<String, Object> map) {
		return sqlSessionTemplate
		        .update("com.dity.dao.DityDaoMapper.updateYjStatus", map);
	}
	
}
