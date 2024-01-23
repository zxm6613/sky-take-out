package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 套餐映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Mapper
public interface SetmealMapper {
    @Select("select COUNT(*) from setmeal where category_id = #{id}")
    int selectCountByCategoryId(Long id);
}
