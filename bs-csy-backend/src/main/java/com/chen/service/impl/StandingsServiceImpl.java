package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.mapper.StandingsMapper;
import com.chen.model.domain.Standings;
import com.chen.service.StandingsService;
import org.springframework.stereotype.Service;

@Service
public class StandingsServiceImpl extends ServiceImpl<StandingsMapper, Standings> implements StandingsService {
}

