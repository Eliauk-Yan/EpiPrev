package com.epiprev.auth.interfaces.dto;

import lombok.Data;

@Data
public class RegisterDTO {

    private String nickName;

    private String password;

    private String email;

}
