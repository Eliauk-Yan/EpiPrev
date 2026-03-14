package com.epiprev.business.forum.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostVO {
    private Long id;
    private Long authorId;
    private String authorName;
    private String title;
    private String content;
    private Integer views;
    private Integer replyCount;
    private LocalDateTime createTime;
    private List<CommentVO> comments;
}
