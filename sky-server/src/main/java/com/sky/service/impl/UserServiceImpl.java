package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现
 *
 * @author 周简coding~~~
 * @date 2024/01/29
 */
@Service
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 登录
     *
     * @param userLoginDTO 用户登录 DTO
     * @return 用户
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //拿到授权码，向微信发送请求，拿到openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode()); //授权码
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(URL, map);//这个map是微信官方提供的appid secret js_code grant_type，第一个参数是地址，第二个是param参数
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        //判断openid是否为空，如果为空，则抛异常
        if (openid == null) throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        //判断这个openid是否存在，如果不存在，就插入数据库
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}
