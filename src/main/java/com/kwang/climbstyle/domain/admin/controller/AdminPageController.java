package com.kwang.climbstyle.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping(value = "/admin/auth/login")
    public String login(){
        return "admin/auth/login";
    }

    @GetMapping(value = "/admin")
    public String mypage(){
        return "admin/index";
    }
}
