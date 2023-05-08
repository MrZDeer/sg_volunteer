package com.sangeng.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ActivityData)表实体类
 *
 * @author makejava
 * @since 2023-04-13 23:43:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_activity_data")
public class ActivityData  {
    @TableId
    private Long id;

    //活动id
    private Long activityId;
    //提交资料志愿者id
    private Long userId;

    //活动名称
    @TableField(exist = false)
    private String title;
    //用户名字
    @TableField(exist = false)
    private String userName;

    //资料地址
    private String dataPath;

    private Integer checked;
    
    private Date createTime;
    
    private Long createId;
    
    private Date updateTime;
    
    private Long updateId;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}

