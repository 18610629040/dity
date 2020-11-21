package com.dity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dity.common.bootonfig.UserLoginToken;

@CrossOrigin
@Controller
@RequestMapping("/dity")
@UserLoginToken
public class DityController {
    @RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
    public String index(){
        return "/welcom/welcomIndex";
    }
}
