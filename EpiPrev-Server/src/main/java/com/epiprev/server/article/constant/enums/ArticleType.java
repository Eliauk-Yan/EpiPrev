package com.epiprev.server.article.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleType {

    IMAGE(0, "图文"),

    VIDEO(1, "视频");

    @EnumValue
    private final Integer code;

    private final String description;

}
