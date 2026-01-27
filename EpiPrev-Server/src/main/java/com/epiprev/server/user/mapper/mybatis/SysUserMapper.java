package com.epiprev.server.user.mapper.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.server.user.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}

