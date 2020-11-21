package com.dity.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dity.common.bootonfig.UserLoginToken;
import com.dity.service.FeePerfMgtService;

@CrossOrigin
@Controller
@RequestMapping("/dity/feePerfMgt")
@UserLoginToken
public class FeePerfMgtController {

	private static final Logger logger = LoggerFactory.getLogger(FeePerfMgtController.class);
	
	@Autowired
	FeePerfMgtService feePerfMgtService;
	

	@RequestMapping(value = "/feePerfIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String gotoLogin(){
        return "/sysMgt/feePerfMgt";
    }
	
	/**
	 * 查询费率数据
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
}
