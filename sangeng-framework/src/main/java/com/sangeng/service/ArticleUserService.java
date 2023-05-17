package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.ArticleUser;
import com.sangeng.domain.vo.PageVo;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public interface ArticleUserService extends IService<ArticleUser> {

    ResponseResult articleListByUserId(Integer pageNum, Integer pageSize, Long userId);

    ResponseResult articleUserSearch(Long userId,Long articleId);

    PageVo selectArticleUserPage(ArticleUser articleUser, Integer pageNum, Integer pageSize);
}
