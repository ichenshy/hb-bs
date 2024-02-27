package com.chen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.ErrorCode;
import com.chen.exception.BusinessException;
import com.chen.mapper.SignMapper;
import com.chen.model.domain.Sign;
import com.chen.model.domain.Standings;
import com.chen.service.SignService;
import com.chen.service.StandingsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Random;

/**
 * @author ChenShengyuan
 * @description 针对表【sign(签到表)】的数据库操作Service实现
 * @createDate 2023-09-16 20:28:39
 */
@Service
@Slf4j
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign>
        implements SignService {
    @Resource
    private StandingsService standingsService;

    /**
     * 每日签到
     *
     * @param userId 用户id
     * @param signDate 签字日期
     * @return {@link Integer}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer sign(Long userId, LocalDate signDate) {
        Sign newSign = new Sign();
        newSign.setUserId(userId);
        newSign.setSignDate(signDate);
        // 随机积分，最大不可超时10积分
        int maxPoints = 10;
        // 创建随机数生成器
        Random random = new Random();
        // 生成随机积分（0到10之间的整数）
        int randomPoints = 1 + random.nextInt(maxPoints + 1);
        log.info("生成随机积分:{}", randomPoints);
        newSign.setFraction(randomPoints);
        Standings standings = new Standings();
        standings.setUserId(userId);
        standings.setFraction(randomPoints);
        boolean success = this.save(newSign);
        QueryWrapper<Standings> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        Standings serviceOne = standingsService.getOne(wrapper);
        boolean result = false;
        if (ObjectUtils.isEmpty(serviceOne)) {
            result = standingsService.save(standings);
        } else {
            standingsService.update().eq("user_id", userId).set("fraction", serviceOne.getFraction() + randomPoints).update();
        }
        if (!success && !result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存失败");
        }
        return randomPoints;
    }
}




