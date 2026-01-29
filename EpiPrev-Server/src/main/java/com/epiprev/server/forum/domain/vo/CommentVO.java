package com.epiprev.server.forum.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommentVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long postId;
    private String content;
    private Long authorId;
    private String authorName;
    private LocalDateTime createTime;
}
