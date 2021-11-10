package com.xyh.controller;

import com.xyh.pojo.Admin;
import com.xyh.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {
    @Resource
    AdminService adminService;
    //实现登录判断，并进行相应的跳转
    @RequestMapping("/login")
    public String login(String name, String pwd, HttpServletRequest request){
        Admin admin = adminService.login(name, pwd);
        if (admin != null) {
            //登录成功
            request.setAttribute("admin",admin);
            return "main";
        }else{
            request.setAttribute("errmsg","用户名或密码不正确");
            return "login";
        }

    }
}
