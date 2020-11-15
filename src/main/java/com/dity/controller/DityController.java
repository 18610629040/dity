package com.dity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@Controller
@RequestMapping("/dity")
public class DityController {
    @RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
    public String holle(){
        return "/index/index";
    }
}
