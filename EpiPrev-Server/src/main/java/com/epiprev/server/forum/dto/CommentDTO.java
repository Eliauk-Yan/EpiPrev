package com.epiprev.server.forum.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long postId;
    private String content;
}

