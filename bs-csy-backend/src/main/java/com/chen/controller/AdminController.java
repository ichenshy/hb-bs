package com.chen.controller;

import com.chen.model.request.UserLoginRequest;
import com.chen.service.AdminService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员登录模块")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    public Map<String, String> admin(@RequestBody UserLoginRequest userLoginRequest) {
        String token = adminService.adminLogin(userLoginRequest);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        return tokenMap;
    }
    @PostMapping("/getInfo")
    public  Map<String, Object> getInfo(HttpServletRequest request) {
        return adminService.getInfo(request);
    }
}


