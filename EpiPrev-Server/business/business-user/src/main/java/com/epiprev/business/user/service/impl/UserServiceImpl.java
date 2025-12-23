package com.epiprev.business.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.business.user.entity.User;
import com.epiprev.business.user.mapper.UserMapper;
import com.epiprev.business.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @classname UserServiceImpl
 * @description 用户服务实现类
 * @date 2025/12/20 20:08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
