package com.sangeng.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.ActivityData;
import com.sangeng.domain.entity.ArticleUser;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.ActivityDataService;
import com.sangeng.service.ArticleUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ActivityData)表控制层
 *
 * @author makejava
 * @since 2023-04-13 23:42:55
 */
@RestController
@RequestMapping("/content/declaration")
public class ActivityUserController {
    /**
     * 服务对象
     */
    @Resource
    private ArticleUserService articleUserService;


    @GetMapping("/list")
    public ResponseResult list(ArticleUser articleUser, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = articleUserService.selectArticleUserPage(articleUser,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }
//
//    @GetMapping(value = "/{id}")
//    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
//        ArticleVo article = activityDataService.getInfo(id);
//        return ResponseResult.okResult(article);
//    }
//
    @PutMapping
    public ResponseResult edit(@RequestBody ArticleUser articleUser){
        boolean res = articleUserService.updateById(articleUser);
        if (res){
            return ResponseResult.okResult();
        }else{
            return ResponseResult.errorResult(500,"update失败");
        }
    }
//    @DeleteMapping("/{id}")
//    public ResponseResult delete(@PathVariable Long id){
//        activityDataService.removeById(id);
//        return ResponseResult.okResult();
//    }
}

