package com.epiprev.common.base.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @classname BaseResponse
 * @description 响应基类
 */
@Data
public class BaseResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String code;

    private String message;

}
