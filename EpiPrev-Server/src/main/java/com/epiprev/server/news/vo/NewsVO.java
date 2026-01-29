package com.epiprev.server.news.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NewsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String summary;

    private String source;

    private String level;

    private String content;

    private LocalDateTime publishTime;

    private LocalDateTime createTime;

}
