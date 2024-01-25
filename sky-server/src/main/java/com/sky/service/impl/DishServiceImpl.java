package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     *
     * @param dishDTO 菜 dto
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
        if (flavors != null && flavors.size() > 0) {  //口味不一定传过去
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishMapper.addDishFlavors(flavors);
        }
    }

    /**
     * 分页
     *
     * @param dishPageQueryDTO dish 页面查询 dto
     * @return 页面结果
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Dish dish = Dish
                .builder()
                .name(dishPageQueryDTO.getName())
                .categoryId(dishPageQueryDTO.getCategoryId())
                .status(dishPageQueryDTO.getStatus())
                .build();
        Page<DishVO> dishes = dishMapper.page(dish);
        long total = dishes.getTotal();
        List<DishVO> records = dishes.getResult();
        return new PageResult(total,records);
    }
}
