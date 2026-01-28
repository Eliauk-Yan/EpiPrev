package com.epiprev.server.forum.domain.dto;

import lombok.Data;

/**
 * @classname PostListDTO
 * @description 论坛列表数据传输对象
 * @date 2026/01/28 12:34
 */
@Data
public class PostListDTO {

    private Long size = 10L;

    private Long current = 1L;

    private String word;

}
