package com.chen.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common.ErrorCode;
import com.chen.exception.BusinessException;
import com.chen.model.domain.RouteInfo;
import com.chen.model.domain.User;
import com.chen.model.request.UserLoginRequest;
import com.chen.service.AdminService;
import com.chen.service.UserService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chen.constants.RedisConstants.LOGIN_ADMIN_KEY;


@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "Shier";

    @Override
    public String adminLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入内容不能为空");
        }
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserAccount, userAccount);
        userWrapper.eq(User::getStatus, 0);
        userWrapper.eq(User::getRole, 1);
        User userInDatabase = userService.getOne(userWrapper);
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        if (userInDatabase == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        if (!userInDatabase.getPassword().equals(encryptPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        String token = UUID.randomUUID().toString(true);
        // 记录用户的登录态
        Gson gson = new Gson();
        String userStr = gson.toJson(userInDatabase);
        stringRedisTemplate.opsForValue().set(LOGIN_ADMIN_KEY + token, userStr);
        stringRedisTemplate.expire(LOGIN_ADMIN_KEY + token, Duration.ofMinutes(15));
        // 4. 记录用户的登录态
        return token;
    }


    @Override
    public Map<String, Object> getInfo(HttpServletRequest request) {
        List<RouteInfo> dynamicRoutes = Arrays.asList(
                new RouteInfo("/home", "Index", "后台首页","HomeFilled"),
                new RouteInfo("/card", "CardData", "卡片数据","UserFilled"),
                new RouteInfo("/table", "TableData", "表格数据","UserFilled"),
                new RouteInfo("/sys/config", "Config", "参数配置","UserFilled"),
                new RouteInfo("/sys/menus", "MenusList", "菜单管理","UserFilled"),
                new RouteInfo("/user", "User", "个人中心","UserFilled"),
                new RouteInfo("/todolist", "Todolist", "签到任务","UserFilled"),
                new RouteInfo("/todolog", "Todolog", "运行日志","UserFilled"),
                new RouteInfo("/todolog", "Todolog", "运行日志1","UserFilled")
        );
        String token = request.getHeader("Token");
        String userStr = stringRedisTemplate.opsForValue().get(LOGIN_ADMIN_KEY + token);
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("menus",dynamicRoutes);
        map.put("userInfo",user);
        return map;

    }


}
