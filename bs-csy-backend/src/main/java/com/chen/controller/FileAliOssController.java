package com.chen.controller;


import com.chen.common.BaseResponse;
import com.chen.common.ErrorCode;
import com.chen.common.ResultUtils;
import com.chen.exception.BusinessException;
import com.chen.model.domain.User;
import com.chen.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.chen.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ChenShengyuan
 * 文件上传接口
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/common")
@CrossOrigin(originPatterns = {"http://localhost:5173", "http://pt.kongshier.top"}, allowCredentials = "true")
public class FileAliOssController {

    @Resource
    private FileService ossService;

    @Resource
    private UserService userService;

    /**
     * 上传
     *
     * @param file    文件
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "file", value = "文件"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> upload(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传文件");
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请登录");
        }

        // 上传到阿里云对象存储
        String fileUrl = ossService.uploadFileAvatar(file);

        User user = new User();
        user.setId(loginUser.getId());
        user.setAvatarUrl(fileUrl);
        boolean success = userService.updateById(user);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
        return ResultUtils.success(fileUrl);
    }
}
