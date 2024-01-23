package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 菜品映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Mapper
public interface DishMapper {
    @Select("select COUNT(*) from dish where category_id = #{id}")
    int selectCountByCategoryId(Long id);
}
