package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜品映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Mapper
public interface DishMapper {
    /**
     * 按类别 ID 查询
     *
     * @param id 编号
     * @return int
     */
    @Select("select COUNT(*) from dish where category_id = #{id}")
    int selectCountByCategoryId(Long id);

    /**
     * 加菜
     *
     * @param dish    菜
     */
    @AutoFill(value = OperationType.INSERT)
    Long addDish(Dish dish);

    /**
     * 添加菜肴口味
     *
     * @param flavors 口味
     */
    void addDishFlavors(@Param("flavors") List<DishFlavor> flavors);

    /**
     * 分页
     *
     * @param dish d
     * @return p
     */
    Page<DishVO> page(Dish dish);
}
