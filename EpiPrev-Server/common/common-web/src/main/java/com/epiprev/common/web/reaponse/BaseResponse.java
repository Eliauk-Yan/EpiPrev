package com.epiprev.common.web.reaponse;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @classname BaseResponse
 * @description 响应基类
 * @date 2025/11/30 13:13
 * @created by YanShijie
 */
@Data
public class BaseResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String code;

    private String message;

}
