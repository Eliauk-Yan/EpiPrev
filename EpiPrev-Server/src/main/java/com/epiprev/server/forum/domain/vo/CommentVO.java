package com.epiprev.server.forum.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private String content;
    private Long authorId;
    private String authorName;
    private LocalDateTime createTime;
}

