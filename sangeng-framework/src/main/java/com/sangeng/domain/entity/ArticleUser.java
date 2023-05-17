package com.sangeng.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章标签关联表(ArticleTag)实体类
 *
 * @author makejava
 * @since 2022-01-15 20:50:54
 */
@TableName(value="sg_article_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleUser implements Serializable {
    private static final long serialVersionUID = 625337492348897098L;

    @TableId
    private Long id;
    /**
    * 活动id
    */
    private Long articleId;
    /**
    * 用户id
    */
    private Long userId;

    private String status;

    //活动名称
    @TableField(exist = false)
    private String title;
    //用户名字
    @TableField(exist = false)
    private String userName;

    //申报时间
    private Date createTime;


}