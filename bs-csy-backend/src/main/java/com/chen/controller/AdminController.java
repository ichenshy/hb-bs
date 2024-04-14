package com.chen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.BaseResponse;
import com.chen.common.ResultUtils;
import com.chen.model.domain.User;
import com.chen.model.request.UserLoginRequest;
import com.chen.model.vo.BlogVO;
import com.chen.model.vo.ChatVO;
import com.chen.model.vo.FriendsVO;
import com.chen.model.vo.SignVO;
import com.chen.model.vo.TeamVO;
import com.chen.model.vo.UserVO;
import com.chen.service.AdminService;
import com.chen.service.BlogCommentsService;
import com.chen.service.BlogService;
import com.chen.service.ChatService;
import com.chen.service.FriendsService;
import com.chen.service.SignService;
import com.chen.service.StandingsService;
import com.chen.service.TeamService;
import com.chen.service.UserService;
import com.chen.service.UserTeamService;
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
    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private FriendsService friendsService;
    @Resource
    private BlogService blogService;
    @Resource
    private BlogCommentsService blogCommentsService;
    @Resource
    private SignService signService;
    @Resource
    private StandingsService standingsService;
    @Resource
    private ChatService chatService;

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

    /**
     * 禁用用户
     *
     * @param id 身份证件
     * @param status 地位
     * @return boolean
     */
    @PostMapping("/disabledUser/{id}/{status}")
    public boolean disabledUser(@PathVariable long id, @PathVariable Integer status) {
        return userService.disabledUser(id, status);
    }

    /**
     * 修改
     *
     * @param user 使用者
     * @return boolean
     */
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
    public BaseResponse<Page<UserVO>> userPageByAdmin(long currentPage, String searchText) {
        Page<UserVO> userVOPage = userService.userPageByAdmin(currentPage, searchText);
        return ResultUtils.success(userVOPage);
    }
    /**
     * 队伍分页
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/teamPageByAdmin")
    @ApiOperation(value = "队伍分页")
    public BaseResponse<Page<TeamVO>> teamPageByAdmin(long currentPage, String searchText) {
        Page<TeamVO> teamVOPage = teamService.teamPageByAdmin(currentPage, searchText);
        return ResultUtils.success(teamVOPage);
    }


    /**
     * 禁用队伍
     *
     * @param id 身份证件
     * @return boolean
     */
    @PostMapping("/disabledTeam/{id}")
    public boolean disabledTeam(@PathVariable long id) {
        return teamService.disabledTeam(id);
    }

    /**
     * 查看加入用户
     *
     * @param id 身份证件
     * @return {@link BaseResponse}
     */
    @GetMapping("/joinUser/{id}")
    public BaseResponse joinUser(@PathVariable long id){
        return teamService.joinUser(id);
    }
    /**
     * 好友申请记录
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/friendByAdmin")
    @ApiOperation(value = "好友申请记录")
    public BaseResponse friendByAdmin(long currentPage, String searchText) {
        Page<FriendsVO> friendPage = friendsService.friendByAdmin(currentPage, searchText);
        return ResultUtils.success(friendPage);
    }
    /**
     * 博客记录
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/blogByAdmin")
    @ApiOperation(value = "博客记录")
    public BaseResponse blogByAdmin(long currentPage, String searchText) {
        Page<BlogVO> blogPage = blogService.blogByAdmin(currentPage, searchText);
        return ResultUtils.success(blogPage);
    }
    /**
     * 签到记录
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/signByAdmin")
    @ApiOperation(value = "签到记录")
    public BaseResponse signByAdmin(long currentPage, String searchText) {
        Page<SignVO> signPage = signService.signByAdmin(currentPage, searchText);
        return ResultUtils.success(signPage);
    }
    /**
     * 好友申请记录
     *
     * @param currentPage 当前页面
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/chatByAdmin")
    @ApiOperation(value = "好友申请记录")
    public BaseResponse chatByAdmin(long currentPage, String searchText) {
        Page<ChatVO> chatPage = chatService.chatByAdmin(currentPage, searchText);
        return ResultUtils.success(chatPage);
    }
}


