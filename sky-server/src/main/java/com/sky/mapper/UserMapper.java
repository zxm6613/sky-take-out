package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

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

    /**
     * 按 ID 选择
     *
     * @param id 编号
     * @return 用户
     */
    @Select("select * from user where id = #{id}")
    User selectById(Long id);

    /**
     * 按 ID 获取
     *
     * @param id 编号
     * @return 用户
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);

    /**
     * 根据map查询用户个数
     *
     * @param map 地图
     * @return 整数
     */
    Integer sumByMap(Map<String, Object> map);
}
