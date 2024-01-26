package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 套餐映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Mapper
public interface SetmealMapper {

    /**
     * 按类别 ID 查询
     *
     * @param id 编号
     * @return int
     */
    @Select("select COUNT(*) from setmeal where category_id = #{id}")
    int selectCountByCategoryId(Long id);

    /**
     * 插入 套餐
     *
     * @param setmeal setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal);

    /**
     * 插入两者关系
     *
     * @param list 列表
     */
    void insertRelationship(@Param("list") List<SetmealDish> list);
}
