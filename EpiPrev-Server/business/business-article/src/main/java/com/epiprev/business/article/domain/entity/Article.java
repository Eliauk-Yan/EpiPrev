package com.epiprev.business.article.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.business.article.domain.enums.ArticleType;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class Article extends BaseEntity {

    private String title;

    private String category;

    private String summary;

    private String content;

    private String cover;

    @TableField("video_url")
    private String videoUrl;

    @TableField("type")
    private ArticleType type;

    private Integer views;
}
