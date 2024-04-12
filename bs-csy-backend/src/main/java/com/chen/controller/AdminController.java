package com.chen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.BaseResponse;
import com.chen.common.ResultUtils;
import com.chen.model.domain.User;
import com.chen.model.request.UserLoginRequest;
import com.chen.model.vo.UserVO;
import com.chen.service.AdminService;
import com.chen.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Map<String, String> admin(@RequestBody UserLoginRequest userLoginRequest) {
        String token = adminService.adminLogin(userLoginRequest);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return tokenMap;
    }

    @PostMapping("/getInfo")
    public Map<String, Object> getInfo(HttpServletRequest request) {
        return adminService.getInfo(request);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        adminService.logout(request);
    }

    /**
     * tag统计
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/statistics")
    public BaseResponse statistics() {
        return userService.echarts();
    }

    @PostMapping("/disabledUser/{id}/{status}")
    public boolean disabledUser(@PathVariable long id, @PathVariable Integer status) {
        return userService.disabledUser(id, status);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    /**
     * 用户分页
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/userPageByAdmin")
    @ApiOperation(value = "用户分页")
    public BaseResponse<Page<UserVO>> userPageByAdmin(long currentPage) {
        Page<UserVO> userVOPage = userService.userPageByAdmin(currentPage);
        return ResultUtils.success(userVOPage);
    }

}


