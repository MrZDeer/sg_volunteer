package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.dto.ArticleDataDto;
import com.sangeng.domain.entity.ActivityData;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.ArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.ActivityDataMapper;
import com.sangeng.service.ActivityDataService;
import com.sangeng.service.ArticleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * (ActivityData)表服务实现类
 *
 * @author makejava
 * @since 2023-04-13 23:43:03
 */
@Service
public class ActivityDataServiceImpl extends ServiceImpl<ActivityDataMapper, ActivityData> implements ActivityDataService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Override
    public PageVo selectActivityDataPage(ActivityData activityData, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<ActivityData> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(activityData.getActivityId() != null,
                ActivityData::getActivityId, activityData.getActivityId());
        queryWrapper.like(activityData.getChecked() != null,
                ActivityData::getChecked, activityData.getChecked());

        Page<ActivityData> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<ActivityData> activityDatas = page.getRecords();

        List<ActivityData> res = activityDatas.stream().map(data -> {
            Article article = articleService.getById(data.getActivityId());
            data.setTitle(article.getTitle());
            String userName = userService.getById(data.getUserId()).getUserName();
            data.setUserName(userName);
            return data;
        }).collect(Collectors.toList());

        //这里偷懒没写VO的转换 应该转换完在设置到最后的pageVo中

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(res);
        return pageVo;
    }
}

