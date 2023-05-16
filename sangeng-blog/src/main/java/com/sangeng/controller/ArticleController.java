package com.sangeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleUser;
import com.sangeng.service.ArticleService;
import com.sangeng.service.ArticleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleUserService articleUserService;


    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){

        ResponseResult result =  articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/articleListByUserId")
    public ResponseResult articleListByUserId(Integer pageNum,Integer pageSize,Long userId){
        return articleUserService.articleListByUserId(pageNum,pageSize,userId);
    }
    @GetMapping("/checkArticleUser")
    public ResponseResult checkArticleUser(Long articleId,Long userId){
        return articleUserService.articleUserSearch(articleId,userId);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @GetMapping("/articleUser/{articleId}")
    public ResponseResult getArticleUser(@PathVariable("articleId") Long articleId){
        return articleService.getArticleUser(articleId);
    }
    @PostMapping("/addVolunteer")
    public ResponseResult addVolunteer(@RequestBody ArticleUser articleUser){
        return articleService.addVolunteer(articleUser);
    }
}
