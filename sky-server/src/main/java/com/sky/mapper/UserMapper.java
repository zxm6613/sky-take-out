package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/29
 */
@Mapper
public interface UserMapper {


    /**
     * 按 OpenID 选择
     *
     * @param openid openid
     * @return 用户
     */
    @Select("select * from user where openid = #{openid}")
    User selectByOpenid(String openid);

    /**
     * 插入
     *
     * @param user 用户
     */
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User selectById(Long id);

    @Select("select * from user where id = #{id}")
    User getById(Long id);
}
