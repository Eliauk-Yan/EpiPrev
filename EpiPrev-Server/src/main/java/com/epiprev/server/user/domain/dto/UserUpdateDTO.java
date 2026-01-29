package com.epiprev.server.user.domain.dto;

import lombok.Data;

/**
 * @classname UserUpdateDTO
 * @description 用户更新信息
 * @date 2026/01/30
 */
@Data
public class UserUpdateDTO {

    private String username;

    private String email;

    private String phone;

    private String avatar;

}
