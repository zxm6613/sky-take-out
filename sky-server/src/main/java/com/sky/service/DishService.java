package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * 菜品服务接口
 *
 * @author 周简coding~~~
 * @date 2024/01/25
 */
public interface DishService {
    /**
     * 加菜
     */
    void addDish(DishDTO dishDTO);

    /**
     * 分页
     *
     * @param dishPageQueryDTO dish 页面查询 dto
     * @return 页面结果
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除批处理
     *
     * @param ids IDS
     */
    void deleteBatch(List<Long> ids);

    /**
     * 按 ID 选择
     *
     * @param id 编号
     * @return 菜 VO
     */
    DishVO selectById(Long id);

    /**
     * 按类别 ID 查询
     *
     * @param categoryId 类别 ID
     * @return 菜
     */
    Dish selectByCategoryId(String categoryId);

    /**
     * 更新菜品的数据
     *
     * @param dishDTO 菜 dto
     */
    void update(DishDTO dishDTO);

    /**
     * 菜是否启用
     *
     * @param status 地位
     */
    void dishEnableOrNot(Integer status,Long id);
}
