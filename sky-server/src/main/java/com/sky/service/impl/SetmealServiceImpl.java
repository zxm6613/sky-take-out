package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 套餐逻辑实现
 *
 * @author 周简coding~~~
 * @date 2024/01/26
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 插入
     *
     * @param setmealDTO Setmeal DTO
     */
    @Transactional
    @Override
    public void insert(SetmealDTO setmealDTO) {
        //先插入套餐，回显主键
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insertSetmeal(setmeal);
        //再插入套餐菜品关系集合，把setmeal_id得设置好
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        list.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealMapper.insertRelationship(list);
    }
}
