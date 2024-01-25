package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

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
}
