package com.dity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DityTestController {
    @RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
    public String holle(){
        return "/index"; 
    }
}
