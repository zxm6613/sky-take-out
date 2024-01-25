package com.sky.service;

import com.sky.dto.DishDTO;

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
}
