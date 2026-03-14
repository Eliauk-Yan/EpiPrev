package com.epiprev.auth.interfaces.vo;

import com.epiprev.common.api.user.response.data.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    private String token;

    private UserInfo userInfo;

}
