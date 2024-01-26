package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

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

    /**
     * 按 ID 选择
     *
     * @param id 编号
     * @return 菜 VO
     */
    DishVO selectById(Long id);

    /**
     * 查询口味
     *
     * @param id 编号
     * @return 菜品风味
     */
    List<DishFlavor> selectFlavors(Long id);

    /**
     * 菜品套餐关系
     *
     * @param ids IDS
     * @return int
     */
    int dishSetmealRelationship(List<Long> ids);

    /**
     * 删除口味
     *
     * @param ids IDS
     */
    void deleteFavory(List<Long> ids);

    /**
     * 按 ID 删除菜品
     *
     * @param ids IDS
     */
    void deleteDishById(List<Long> ids);

    /**
     * 按类别 ID 选择
     *
     * @param categoryId 类别 ID
     * @return 菜
     */
    List<Dish> selectByCategoryId(Long categoryId);


    /**
     * 更新菜品的口味
     *
     * @param flavors flavors
     */
    void updateFlavors(List<DishFlavor> flavors);

    /**
     * 更新菜品的数据
     *
     * @param dish 菜
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 删除一个菜品
     *
     * @param id 编号
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteOneFavory(Long id);

    /**
     * 添加菜肴风味
     *
     * @param flavor 味道
     */
    @Insert("insert into dish_flavor (dish_id, name, value) VALUES (#{dishId},#{name},#{value})")
    void addDishFlavor(DishFlavor flavor);
}
