package com.epiprev.business.user.api.response;

import lombok.Data;

/**
 * @description 用户实名认证响应
 */
@Data
public class RealNameAuthResponse {

    private String msg;

    private Boolean success;

    private Integer code;

    private DataInfo data;

    @Data
    public static class DataInfo {

        private String birthday;

        private Integer result;

        private String address;

        private String orderNo;

        private String sex;

        private String desc;

    }
}