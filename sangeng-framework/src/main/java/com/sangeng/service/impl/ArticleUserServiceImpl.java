package com.sangeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.entity.ArticleUser;
import com.sangeng.mapper.ArticleUserMapper;
import com.sangeng.service.ArticleUserService;
import org.springframework.stereotype.Service;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Service
public class ArticleUserServiceImpl extends ServiceImpl<ArticleUserMapper, ArticleUser> implements ArticleUserService {
}
