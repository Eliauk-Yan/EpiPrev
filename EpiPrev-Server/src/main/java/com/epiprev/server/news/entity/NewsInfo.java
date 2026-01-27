package com.epiprev.server.news.entity;
import com.epiprev.server.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
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

    private String level;

    private String content;

    private LocalDateTime publishTime;

}


