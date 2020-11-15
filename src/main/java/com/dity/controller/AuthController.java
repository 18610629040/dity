package com.dity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dity.common.aop.annotation.Log;
import com.dity.common.bootonfig.TokenService;
import com.dity.common.bootonfig.TokenUtil;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.service.AuthService;

@CrossOrigin
@Controller
@RequestMapping("/dity/auth")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthService authService;
	
	@Autowired
	TokenService tokenService;
	
	@RequestMapping(value = "/gotoLogin", method = { RequestMethod.POST, RequestMethod.GET })
    public String gotoLogin(){
        return "/login/login";
    }
	
	@RequestMapping(value = "/gotoRegister", method = { RequestMethod.POST, RequestMethod.GET })
    public String gotoRegister(){
        return "/login/register";
    }
	
    @RequestMapping("/notLogin")
    @ResponseBody
    public Object notLogin() {
        Map<String, Object> rmap = new HashMap<>();
        rmap.put("O_RUNSTATUS", 500);
        rmap.put("O_MSG", "无效session");
        
        return rmap;
    }
    
    @UserLoginToken
    @RequestMapping("/getMessage")
    @ResponseBody
    @Log(content="验证session")
	public Object getMessage(HttpServletRequest request) {
    	Map<String, Object> rmap = new HashMap<>();
    	HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 取出token中带的用户id 进行操作
		rmap.put("O_RUNSTATUS", 1);
		rmap.put("O_DATA", "token通过验证，当前登录人编号："+TokenUtil.getTokenUserId(token));
		return rmap;
	}
    
    
    @SuppressWarnings("unchecked")
	@RequestMapping("/login")
    @ResponseBody
    @Log(content="登录")
    public Object login(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "USER_ID", required = false) String userId,
            @RequestParam(value = "PASS", required = false) String pass) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> rmap = new HashMap<>();
        try {
            map.put("USER_ID", userId);
            List<Map<String, Object>> list = (List<Map<String, Object>>) authService.getUserInfo(map);
            if(list.isEmpty()) {
            	rmap.put("O_RUNSTATUS", 0);
                rmap.put("O_MSG", "登录失败,账号不存在！");
                return rmap;
            }
            Map<String, Object> user = list.get(0);
            if (!pass.equals((String)user.get("PASS"))) {
    			rmap.put("O_RUNSTATUS", 0);
                rmap.put("O_MSG", "登录失败,密码错误！");
                return rmap;
    		}
            String token = tokenService.getToken(user);
			request.getSession().setAttribute("userInfo", user);
			request.getSession().setAttribute("token", token);
			rmap.put("O_DATA", user);
            rmap.put("O_RUNSTATUS", 1);
            rmap.put("O_MSG", "");
        } catch (Exception e) {
            logger.error("/login:" + map, e);
            rmap.put("O_RUNSTATUS", -1);
            rmap.put("O_MSG", "system error");
        }
        return rmap;
    }
    
	@RequestMapping("/register")
    @Log(content="注册")
	@ResponseBody
    public Object register(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "username", required = false) String name,
            @RequestParam(value = "mobileno", required = false) String mobile,
            @RequestParam(value = "userpass", required = false) String pass,
            @RequestParam(value = "userpass1", required = false) String pass1) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(!pass.equals(pass1)) {
            	map.put("O_RUNSTATUS", 0);
            	map.put("O_MSG", "请核对再次输入密码！");
                return map;
            }
            map.put("O_RUNSTATUS", 1);
            map.put("O_MSG", "");
        } catch (Exception e) {
            logger.error("/register:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
}
