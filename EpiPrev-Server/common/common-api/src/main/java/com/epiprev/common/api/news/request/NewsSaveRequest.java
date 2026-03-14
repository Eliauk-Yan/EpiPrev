package com.epiprev.common.api.news.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻保存/更新请求
 */
@Data
public class NewsSaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String summary;

    private String source;

    /**
     * 新闻等级，如 info / warning 等
     */
    private String level;

    private String content;

    private LocalDateTime publishTime;
}
