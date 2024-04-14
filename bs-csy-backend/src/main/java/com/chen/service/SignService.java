package com.chen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.domain.Sign;
import com.chen.model.vo.SignVO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
* @author Shier
* @description 针对表【sign(签到表)】的数据库操作Service
* @createDate 2023-09-16 20:28:39
*/
public interface SignService extends IService<Sign> {
    /**
     * 每日签到
     *
     * @param userId 用户id
     * @param signDate 签字日期
     * @return {@link Integer}
     */
    @Transactional(rollbackFor = Exception.class)
    Integer sign(Long userId, LocalDate signDate);

    Page<SignVO> signByAdmin(long currentPage, String searchText);
}
