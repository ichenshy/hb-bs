package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.mapper.TagMapper;
import com.chen.model.domain.Tag;
import com.chen.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author Shier
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2023-05-07 19:05:01
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}




