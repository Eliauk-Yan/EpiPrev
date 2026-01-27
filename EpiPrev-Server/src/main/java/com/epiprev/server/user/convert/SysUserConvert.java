package com.epiprev.server.user.convert;

import com.epiprev.server.user.domain.entity.SysUser;
import com.epiprev.server.user.domain.dto.UserInfoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysUserConvert {

    UserInfoDTO sysUserToUserInfoDTO(SysUser sysUser);

}
