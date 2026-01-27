package com.epiprev.server.auth.domain.vo;

import com.epiprev.server.user.domain.dto.UserInfoDTO;
import lombok.Data;

/**
 * @classname LoginVO
 * @description 登录返回对象
 * @date 2026/01/27 18:21
 */
@Data
public class LoginVO {

    private String token;

    private UserInfoDTO userInfo;

}
