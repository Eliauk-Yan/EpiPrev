package com.epiprev.business.article.domain.enums;

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

    public static ArticleType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ArticleType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

}
