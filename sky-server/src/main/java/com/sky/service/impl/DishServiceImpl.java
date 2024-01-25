package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 碟品服务实现
 *
 * @author 周简coding~~~
 * @date 2024/01/25
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    /**
     * 插入菜品和菜品口味
     */
    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        //先插入菜品数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        //再插入菜品口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size()>0){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishMapper.addDishFlavors(flavors);
        }
    }
}
