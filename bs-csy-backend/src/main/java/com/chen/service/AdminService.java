package com.chen.service;

import com.chen.model.request.UserLoginRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AdminService {
    String adminLogin(UserLoginRequest userLoginRequest);

    Map<String, Object> getInfo(HttpServletRequest request);
}
