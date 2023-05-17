package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.ActivityData;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleUser;
import com.sangeng.domain.entity.LoginUser;
import com.sangeng.domain.vo.ArticleListVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.ArticleUserMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.ArticleUserService;
import com.sangeng.service.CategoryService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Service
public class ArticleUserServiceImpl extends ServiceImpl<ArticleUserMapper, ArticleUser> implements ArticleUserService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult articleListByUserId(Integer pageNum, Integer pageSize, Long userId) {
        //查询条件
        LambdaQueryWrapper<ArticleUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Objects.nonNull(userId)&&userId>0 ,ArticleUser::getUserId,userId);

        List<ArticleUser> list = list(lambdaQueryWrapper);

        List<Long> articleIds = list.stream().map(item -> item.getArticleId()).collect(Collectors.toList());

        List<Article> articles = articleService.listByIds(articleIds);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page.setRecords(articles);

        //查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult articleUserSearch(Long articleId,Long userId) {
        //查询条件
        LambdaQueryWrapper<ArticleUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Objects.nonNull(userId)&&userId>0 ,ArticleUser::getUserId,userId)
                        .eq(Objects.nonNull(articleId)&&articleId>0 ,ArticleUser::getArticleId,articleId);

//        ArticleUser one = getOne(lambdaQueryWrapper);

        List<ArticleUser> list = list(lambdaQueryWrapper);


        return ResponseResult.okResult(list);
    }

    @Override
    public PageVo selectArticleUserPage(ArticleUser articleUser, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<ArticleUser> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(articleUser.getArticleId() != null,
                ArticleUser::getArticleId, articleUser.getArticleId());

        Page<ArticleUser> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);


        //转换成VO
        List<ArticleUser> articleUsers = page.getRecords();

        List<ArticleUser> res = articleUsers.stream().map(data -> {
            Article article = articleService.getById(data.getArticleId());
            data.setTitle(article.getTitle());
            String userName = userService.getById(data.getUserId()).getUserName();
            data.setUserName(userName);
            return data;
        }).collect(Collectors.toList());

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(res);
        return pageVo;
    }

}
