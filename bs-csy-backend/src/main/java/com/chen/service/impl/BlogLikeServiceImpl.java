package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.mapper.BlogLikeMapper;
import com.chen.model.domain.BlogLike;
import com.chen.service.BlogLikeService;
import org.springframework.stereotype.Service;

/**
* @author Shier
* @description 针对表【blog_like】的数据库操作Service实现
* @createDate 2023-06-05 21:54:55
*/
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike>
    implements BlogLikeService {

}




