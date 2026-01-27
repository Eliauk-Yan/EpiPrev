package com.epiprev.server.user.domain.dto;

import lombok.Data;

/**
 * @classname UserInfoDTO
 * @description 用户信息
 * @date 2026/01/27 18:42
 */
@Data
public class UserInfoDTO {

    private Long id;

    private String username;

    private String email;

    private String avatar;

}
