package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * 用户服务
 *
 * @author 周简coding~~~
 * @date 2024/01/29
 */
public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
