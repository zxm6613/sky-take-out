package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    @Select("select COUNT(*) from dish where category_id = #{id}")
    int selectCountByCategoryId(Long id);
}
