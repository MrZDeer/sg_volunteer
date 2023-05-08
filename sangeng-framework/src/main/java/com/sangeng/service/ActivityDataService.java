package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.entity.ActivityData;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.PageVo;


/**
 * (ActivityData)表服务接口
 *
 * @author makejava
 * @since 2023-04-13 23:43:02
 */
public interface ActivityDataService extends IService<ActivityData> {

    PageVo selectActivityDataPage(ActivityData activityData, Integer pageNum, Integer pageSize);
}

