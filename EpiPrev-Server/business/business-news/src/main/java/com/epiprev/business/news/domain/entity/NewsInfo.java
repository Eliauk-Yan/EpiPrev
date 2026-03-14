package com.epiprev.business.news.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("news_info")
public class NewsInfo extends BaseEntity {

    private String title;

    private String summary;

    private String source;

    /**
     * 新闻等级，例如 info / warning 等
     */
    private String level;

    private String content;

    @TableField("publish_time")
    private LocalDateTime publishTime;
}

