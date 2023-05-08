package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDataDto {

    private Long id;
    //活动名称
    private String activityName;
    //用户名字
    private String userName;
    //文件
    private String data;

    //状态（0通过，1不通过，2待审核）
    private String status;

}
