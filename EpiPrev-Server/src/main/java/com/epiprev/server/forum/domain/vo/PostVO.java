package com.epiprev.server.forum.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String content;

    private Long authorId;

    private String authorName;

    private Integer views;

    private Integer replies;

    private LocalDateTime createTime;

    private List<CommentVO> comments;

}
