package com.epiprev.server.auth.domain.dto;

import lombok.Data;

/**
 * @classname RegisterDTO
 * @description 注册
 * @date 2026/01/27 19:23
 */
@Data
public class RegisterDTO {

    private String username;

    private String password;

    private String email;

    private String phone;

}
