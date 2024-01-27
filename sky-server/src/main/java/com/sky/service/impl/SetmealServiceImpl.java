package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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

    /**
     * 分页
     *
     * @param setmealPageQueryDTO setmeal 页面查询 dto
     * @return 页面结果
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        //分页参数的设置
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealPageQueryDTO, setmeal);
        Page<SetmealVO> page = setmealMapper.page(setmeal);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除批处理
     *
     * @param ids IDS
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //起售状态的不能删除
        ids.forEach(id -> {
            int count = setmealMapper.selectStatus(id);
            if (count > 0) throw new BaseException(MessageConstant.SETMEAL_ON_SALE);
        });
        //先删除套餐菜品关系数据
        setmealMapper.deleteRelationship(ids);
        //再删除套餐数据
        setmealMapper.delete(ids);
    }

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return Setmeal DTO
     */
    @Override
    public SetmealDTO selectById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal, setmealDTO);
        List<SetmealDish> list = setmealMapper.selectRelationship(id);
        setmealDTO.setSetmealDishes(list);
        return setmealDTO;
    }

    /**
     * 更新
     *
     * @param setmealDTO Setmeal DTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        //如果正常修改的话，那就比较繁琐，所以对于套餐菜品关系表，采取先删除后插入的方法
        //先删除关系，后插入关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        List<Long> ids = new ArrayList<>();
        ids.add(setmealDTO.getId());
        setmealMapper.deleteRelationship(ids);
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
        setmealMapper.insertRelationship(setmealDishes);
        //再修改套餐其他数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
    }

    /**
     * 启用与否
     *
     * @param status 地位
     */
    @Override
    public void enableOrNot(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder().status(status).id(id).build();
        setmealMapper.update(setmeal);
    }
}
