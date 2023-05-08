package com.sangeng.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.entity.ActivityData;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.ArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.ActivityDataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (ActivityData)表控制层
 *
 * @author makejava
 * @since 2023-04-13 23:42:55
 */
@RestController
@RequestMapping("/content/activityData")
public class ActivityDataController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityDataService activityDataService;


    @GetMapping("/list")
    public ResponseResult list(ActivityData activityData, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = activityDataService.selectActivityDataPage(activityData,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }
//
//    @GetMapping(value = "/{id}")
//    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
//        ArticleVo article = activityDataService.getInfo(id);
//        return ResponseResult.okResult(article);
//    }
//
//    @PutMapping
//    public ResponseResult edit(@RequestBody ArticleDto article){
//        activityDataService.edit(article);
//        return ResponseResult.okResult();
//    }
//    @DeleteMapping("/{id}")
//    public ResponseResult delete(@PathVariable Long id){
//        activityDataService.removeById(id);
//        return ResponseResult.okResult();
//    }
}

