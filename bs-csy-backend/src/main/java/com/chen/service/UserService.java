package com.chen.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.domain.RouteInfo;
import com.chen.model.domain.User;
import com.chen.model.request.UserUpdateRequest;
import com.chen.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ChenShengyuan
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-05-07 19:56:01
 */
public interface UserService extends IService<User> {
    /**
     * 注册
     *
     * @param phone 电话
     * @param userAccount 用户帐户
     * @param userPassword 用户密码
     * @param checkPassword 检查密码
     * @param code 密码
     * @return long
     */
    long userRegister(String phone, String userAccount, String userPassword, String checkPassword,String code);

    /**
     * 用户登录
     *
     * @param userAccount 用户帐户
     * @param userPassword 用户密码
     * @param request 要求
     * @return {@link String}
     */
    String userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取安全用户
     *
     * @param originUser 原始用户
     * @return {@link User}
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request 要求
     * @return int
     */
    int userLogout(HttpServletRequest request);

    /**
     * 按标签搜索用户
     *
     * @param tagNameList 标记名称列表
     * @param currentPage 当前页面
     * @return {@link Page}<{@link User}>
     */
    Page<User> searchUsersByTags(List<String> tagNameList, long currentPage);

    /**
     * 是管理员
     *
     * @param loginUser 登录用户
     * @return boolean
     */
    boolean isAdmin(User loginUser);

    /**
     * 更新用户
     *
     * @param user 使用者
     * @param request 要求
     * @return boolean
     */
    boolean updateUser(User user, HttpServletRequest request);

    /**
     * 用户页面
     *
     * @param currentPage 当前页面
     * @return {@link Page}<{@link UserVO}>
     */
    Page<UserVO> userPage(long currentPage);

    /**
     * 获取登录用户
     *
     * @param request 要求
     * @return {@link User}
     */
    User getLoginUser(HttpServletRequest request);

//    List<User> matchUsers(long num, User user);

    /**
     * 是登录名
     *
     * @param request 要求
     * @return {@link Boolean}
     */
    Boolean isLogin(HttpServletRequest request);

    Page<UserVO> matchUser(long currentPage, User loginUser);

    UserVO getUserById(Long userId, Long loginUserId);

    List<String> getUserTags(Long id);

    void updateTags(List<String> tags, Long userId);

    void updateUserWithCode(UserUpdateRequest updateRequest, Long userId);

    Page<UserVO> getRandomUser();

    void updatePassword(String phone, String password, String confirmPassword,String code);

    Page<UserVO> preMatchUser(long currentPage, String username, User loginUser);

    List<RouteInfo> selectMenus();

}
