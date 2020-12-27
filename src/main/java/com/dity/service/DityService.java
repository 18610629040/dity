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
	
	public int qryGoodsListCt(Map<String, Object> map) {
		return dityDao.qryGoodsListCt(map);
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
	
	public int setGoodPrice(Map<String, Object> map) {
		return dityDao.setGoodPrice(map);
	}

	public int delOrder(Map<String, Object> map) {
		return dityDao.delOrder(map);
	}
	
	public int editGoodsYsc(Map<String, Object> map) {
		return dityDao.editGoodsYsc(map);
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
	
	public int editGoodsMsPdStatus(Map<String, Object> map) {
		return dityDao.editGoodsMsPdStatus(map);
	}
	
	public int editGoodsPdStatus(Map<String, Object> map) {
		return dityDao.editGoodsPdStatus(map);
	}
	
	public List<Map<String,Object>> qryMyOrder(Map<String, Object> map) {
		return dityDao.qryMyOrder(map);
	}
	
	public int setUserWtsk(Map<String, Object> map) {
		return dityDao.setUserWtsk(map);
	}
	
	public int clearUserWtsk(Map<String, Object> map) {
		return dityDao.clearUserWtsk(map);
	}
	
	public List<Map<String,Object>> qryWtSkInfo(Map<String, Object> map) {
		return dityDao.qryWtSkInfo(map);
	}
	
	public List<Map<String,Object>> qryOrderSkInfo(Map<String, Object> map) {
		return dityDao.qryOrderSkInfo(map);
	}
	
	public List<Map<String,Object>> checkOrder(Map<String, Object> map) {
		return dityDao.checkOrder(map);
	}
	
	public int clearUserSk(Map<String, Object> map) {
		return dityDao.clearUserSk(map);
	}

	public List<Map<String,Object>> getYjInfo(Map<String, Object> map) {
		return dityDao.getYjInfo(map);
	}
	
	public List<Map<String,Object>> getYjList(Map<String, Object> map) {
		return dityDao.getYjList(map);
	}
	public int addYJ(Map<String, Object> map) {
		return dityDao.addYJ(map);
	}
	
	public int updateYjStatus(Map<String, Object> map) {
		return dityDao.updateYjStatus(map);
	}
}
