package com.epiprev.server.forum.domain.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long postId;
    private String content;
}

