package com.dity.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dity.common.bootonfig.TokenUtil;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.IDUtils;
import com.dity.service.FeePerfMgtService;

@CrossOrigin
@Controller
@RequestMapping("/dity/feePerfMgt")
@UserLoginToken
public class FeePerfMgtController {

	private static final Logger logger = LoggerFactory.getLogger(FeePerfMgtController.class);
	
	@Autowired
	FeePerfMgtService feePerfMgtService;
	
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
	public Object modfyFeePerfData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String RATE_NAME = request.getParameter("RATE_NAME");
		String RATE_VAL = request.getParameter("RATE_VAL");
		String oper = request.getParameter("oper");
		String msg = "操作成功！";
		int count = 0;
		try {
			map.put("RATE_NAME",RATE_NAME);
			map.put("RATE_VAL",RATE_VAL);
			map.put("CRITE_USER","");
			if("add".equals(oper)){
				map.put("ID",IDUtils.createID());
				count = feePerfMgtService.addFeePerfData(map);
			}else if("edit".equals(oper)) {
				map.put("ID",request.getParameter("ID"));
				count = feePerfMgtService.modfyFeePerfData(map);
			}else {
				map.put("ID",request.getParameter("ID"));
				count = feePerfMgtService.delFeePerfData(map);
			}
			map.put("O_RUNSTATUS",count);
			if(count<1) {
				msg = "操作失败，请联系管理员！";
			}
		} catch (Exception e) {
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","操作失败，请联系管理员！");
			logger.error("/dity/feePerfMgt/modfyFeePerfData:"+map,e);
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

	/**
	 * 信息提示管理首页
	 */
	@RequestMapping(value = "/infoTsIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String infoTsIndex(){
        return "/sysMgt/infoTsMgt";
    }
}
