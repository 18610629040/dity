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
	
	
	public List<Map<String,Object>> qryGoodsMsList(Map<String, Object> map) {
		return dityDao.qryGoodsMsList(map);
	}
	
	public int addGoodsMs(Map<String, Object> map) {
		return dityDao.addGoodsMs(map);
	}
	
	public int editGoodsMs(Map<String, Object> map) {
		return dityDao.editGoodsMs(map);
	}
	
	public int setGoodsMsStatus(Map<String, Object> map) {
		return dityDao.setGoodsMsStatus(map);
	}
	
	public int delGoodsMs(Map<String, Object> map) {
		return dityDao.delGoodsMs(map);
	}
	
	

	public List<Map<String,Object>> qryGoodsList(Map<String, Object> map) {
		return dityDao.qryGoodsList(map);
	}
	
	public int addGoods(Map<String, Object> map) {
		return dityDao.addGoods(map);
	}
	
	public int editGoods(Map<String, Object> map) {
		return dityDao.editGoods(map);
	}
	
	public int setGoodsStatus(Map<String, Object> map) {
		return dityDao.setGoodsStatus(map);
	}
	
	public int delGoods(Map<String, Object> map) {
		return dityDao.delGoods(map);
	}
	
	
	public List<Map<String,Object>> qryUserDz(Map<String, Object> map) {
		return dityDao.qryUserDz(map);
	}
	
	public int addUserDz(Map<String, Object> map) {
		return dityDao.addUserDz(map);
	}
	
	public int editUserDz(Map<String, Object> map) {
		return dityDao.editUserDz(map);
	}
	
	public int setUserDzMr(Map<String, Object> map) {
		return dityDao.setUserDzMr(map);
	}
	public void setUserDzMr2(Map<String, Object> map) {
		dityDao.setUserDzMr2(map);
	}
	
	public int delUserDz(Map<String, Object> map) {
		return dityDao.delUserDz(map);
	}
	
	public List<Map<String,Object>> qryOrder(Map<String, Object> map) {
		return dityDao.qryOrder(map);
	}
	
	public List<Map<String,Object>> qryFsOrder(Map<String, Object> map) {
		return dityDao.qryFsOrder(map);
	}

	public int addOrder(Map<String, Object> map) {
		return dityDao.addOrder(map);
	}

	public int setOrder(Map<String, Object> map) {
		return dityDao.setOrder(map);
	}

	public int delOrder(Map<String, Object> map) {
		return dityDao.delOrder(map);
	}
	
	public int editGoodsMsOwnAcnt(Map<String, Object> map) {
		return dityDao.editGoodsMsOwnAcnt(map);
	}
	
	public int editGoodsOwnAcnt(Map<String, Object> map) {
		return dityDao.editGoodsOwnAcnt(map);
	}
	
	public int setOrderExpress(Map<String, Object> map) {
		return dityDao.setOrderExpress(map);
	}
	
	public List<Map<String,Object>> qryFsUser(Map<String, Object> map) {
		return dityDao.qryFsUser(map);
	}
}
