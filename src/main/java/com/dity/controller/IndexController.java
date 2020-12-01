package com.dity.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.SessionUtil;

@CrossOrigin
@Controller
@UserLoginToken
public class IndexController {
	
    @RequestMapping("/dity")
	public ModelAndView index(HttpServletRequest request,ModelAndView mv){
    	mv.addAllObjects(SessionUtil.getUser());
    	mv.setViewName("/welcom/welcomIndex");
        return mv;
    }
}
