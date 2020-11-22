package com.dity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dity.common.bootonfig.UserLoginToken;

@CrossOrigin
@Controller
@RequestMapping("/dity")
@UserLoginToken
public class DityController {
	
	private static final Logger logger = LoggerFactory.getLogger(DityController.class);
	
    @RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
    public String index(){
        return "/welcom/welcomIndex";
    }
    
    @RequestMapping(value = "/userIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String userIndex(){
        return "/sysMgt/userIndex";
    }
    
	@SuppressWarnings("unchecked")
	@RequestMapping("/userMgt")
	@ResponseBody
    public Object userMgt(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "postData", required = false) Object postData) {
        Map<String, Object> map = new HashMap<>();
        try {
        	Map<String, Object> data = (Map<String, Object>)JSON.parse(String.valueOf(postData));
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/login:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
}
