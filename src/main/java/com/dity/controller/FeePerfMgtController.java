package com.dity.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dity.common.SysProperties;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.IDUtils;
import com.dity.common.utils.SessionUtil;
import com.dity.service.FeePerfMgtService;

@CrossOrigin
@Controller
@RequestMapping("/dity/feePerfMgt")
@UserLoginToken
public class FeePerfMgtController {

	private static final Logger logger = LoggerFactory.getLogger(FeePerfMgtController.class);
	
	@Autowired
	FeePerfMgtService feePerfMgtService;
	
	@Autowired
    SysProperties sysProperties;
	
	/**
	 * 费率管理首页
	 */
	@RequestMapping(value = "/feePerfIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String feePerfIndex(){
        return "/sysMgt/feePerfMgt";
    }
	
	/**
	 * 费率数据-查询
	 */
	@RequestMapping(value = "/srchFeePerfData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchFeePerfData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchFeePerfData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchFeePerfData:"+map,e);
		}
		return list;
	}

	/**
	 * 费率数据-操作
	 */
	@RequestMapping(value = "/optFeePerfData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object optFeePerfData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String postData = request.getParameter("postData");
		JSONObject jObj = JSONObject.parseObject(postData);
		String RATE_NAME = String.valueOf(jObj.get("RATE_NAME"));
		String RATE_VAL = String.valueOf(jObj.get("RATE_VAL"));
		String oper = String.valueOf(jObj.get("oper"));
		String msg = "操作成功！";
		int count = 0;
		try {
			map.put("RATE_NAME",RATE_NAME);
			map.put("RATE_VAL",RATE_VAL);
			map.put("CRITE_USER","admin");
			if("add".equals(oper)){
				map.put("ID",IDUtils.createID());
				count = feePerfMgtService.addFeePerfData(map);
			}else if("edit".equals(oper)) {
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.modfyFeePerfData(map);
			}else if("del".equals(oper)){
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.delFeePerfData(map);
			}
			map.put("O_RUNSTATUS",count);
			if(count<1) {
				msg = "操作失败，请联系管理员！";
			}
		} catch (Exception e) {
			map.put("O_RUNSTATUS",0);
			msg = "操作失败，请联系管理员！";
			logger.error("/dity/feePerfMgt/optFeePerfData:"+map,e);
		}
		map.put("O_MSG",msg);
		return map;
	}

	/**
	 * 商品类别维护管理首页
	 */
	@RequestMapping(value = "/prdtLbIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String prdtLbIndex(){
        return "/sysMgt/prdtLbMgt";
    }
	
	@RequestMapping(value = "/pdType", method = { RequestMethod.POST, RequestMethod.GET })
    public String pdType(){
        return "/sysMgt//pdType";
    }
	
	/**
	 * 商品类别数据-查询
	 */
	@RequestMapping(value = "/srchPrdtLbData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchPrdtLbData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchPrdtLbData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchPrdtLbData:"+map,e);
		}
		return list;
	}
	
	@RequestMapping("/addQy")
	@ResponseBody
    public Object addQy(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id,
            @RequestParam(value = "TYPE_NAME", required = false) String TYPE_NAME,
            @RequestParam(value = "TYPE_ORDER", required = false) String TYPE_ORDER,
            @RequestParam(value = "STATUS", required = false) String STATUS,
            @RequestParam(value = "FILE_URL", required = false) String FILE_URL) {
        Map<String, Object> map = new HashMap<>();
        try {
        	if(StringUtils.isBlank(FILE_URL)) {
        		map.put("O_RUNSTATUS", -1);
                map.put("O_MSG", "请选择图片");
                return map;
        	}
        	if(StringUtils.isBlank(id)) {
        		map.put("ID",IDUtils.createID());
        	}else {
        		map.put("ID",id);
        	}
        	map.put("TYPE_NAME",TYPE_NAME);
        	map.put("TYPE_ORDER",TYPE_ORDER);
        	map.put("FILE_URL",FILE_URL);
        	map.put("STATUS",STATUS);
        	map.put("CRITE_USER",SessionUtil.getUserNo());
        	if(StringUtils.isBlank(id)) {
        		feePerfMgtService.addPrdtLbData(map);
        	}else {
        		feePerfMgtService.modfyPrdtLbData(map);
        	}
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addUser:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	/**
	 * 商品类别数据-操作
	 */
	@RequestMapping(value = "/optPrdtLbData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object optPrdtLbData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String postData = request.getParameter("postData");
		JSONObject jObj = JSONObject.parseObject(postData);
		String TYPE_ORDER = String.valueOf(jObj.get("TYPE_ORDER"));
		String TYPE_NAME = String.valueOf(jObj.get("TYPE_NAME"));
		String oper = String.valueOf(jObj.get("oper"));
		String msg = "操作成功！";
		int count = 0;
		try {
			map.put("TYPE_ORDER",TYPE_ORDER);
			map.put("TYPE_NAME",TYPE_NAME);
			map.put("CRITE_USER","admin");
			if("add".equals(oper)){
				map.put("ID",IDUtils.createID());
				count = feePerfMgtService.addPrdtLbData(map);
			}else if("edit".equals(oper)) {
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.modfyPrdtLbData(map);
			}else if("del".equals(oper)){
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.delPrdtLbData(map);
			}
			map.put("O_RUNSTATUS",count);
			if(count<1) {
				msg = "操作失败，请联系管理员！";
			}
		} catch (Exception e) {
			map.put("O_RUNSTATUS",0);
			msg = "操作失败，请联系管理员！";
			logger.error("/dity/feePerfMgt/optPrdtLbData:"+map,e);
		}
		map.put("O_MSG",msg);
		return map;
	}

	/**
	 * 信息提示管理首页
	 */
	@RequestMapping(value = "/infoTsIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String infoTsIndex(){
        return "/sysMgt/infoTsMgt";
    }
	
	/**
	 * 信息提示数据-查询
	 */
	@RequestMapping(value = "/srchInfoTsData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchInfoTsData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchInfoTsData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchInfoTsData:"+map,e);
		}
		return list;
	}
	
	/**
	 * 信息提示数据-操作
	 */
	@RequestMapping(value = "/optInfoTsData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object optInfoTsData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String postData = request.getParameter("postData");
		JSONObject jObj = JSONObject.parseObject(postData);
		String CONTATE_NAME = String.valueOf(jObj.get("CONTATE_NAME"));
		String CONTATE_INFO = String.valueOf(jObj.get("CONTATE_INFO"));
		String oper = String.valueOf(jObj.get("oper"));
		String msg = "操作成功！";
		int count = 0;
		try {
			map.put("CONTATE_NAME",CONTATE_NAME);
			map.put("CONTATE_INFO",CONTATE_INFO);
			map.put("CRITE_USER","admin");
			if("add".equals(oper)){
				map.put("ID",IDUtils.createID());
				count = feePerfMgtService.addInfoTsData(map);
			}else if("edit".equals(oper)) {
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.modfyInfoTsData(map);
			}else if("del".equals(oper)){
				map.put("O_MSG","APP端信息提示，请勿删除！");
				map.put("O_RUNSTATUS",-1);
				return map;
//				map.put("ID",String.valueOf(jObj.get("ID")));
//				count = feePerfMgtService.delInfoTsData(map);
			}
			map.put("O_RUNSTATUS",count);
			if(count<1) {
				msg = "操作失败，请联系管理员！";
			}
		} catch (Exception e) {
			map.put("O_RUNSTATUS",0);
			msg = "操作失败，请联系管理员！";
			logger.error("/dity/feePerfMgt/optInfoTsData:"+map,e);
		}
		map.put("O_MSG",msg);
		return map;
	}
	
	/**
	 * 订单管理首页
	 */
	@RequestMapping(value = "/pdOrderIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String pdOrderIndex(){
        return "/sysMgt/pdOrderMgt";
    } 
	
	/**
	 * 订单管理-查询
	 */
	@RequestMapping(value = "/srchPdOrderData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchPdOrderData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchPdOrderData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchPdOrderData:"+map,e);
		}
		return list;
	}
}
