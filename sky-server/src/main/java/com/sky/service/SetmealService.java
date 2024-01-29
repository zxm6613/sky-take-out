package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

import java.util.List;

/**
 * 套餐服务
 *
 * @author 周简coding~~~
 * @date 2024/01/26
 */
public interface SetmealService {
    /**
     * 插入
     *
     * @param setmealDTO Setmeal DTO
     */
    void insert(SetmealDTO setmealDTO);

    /**
     * 分页
     *
     * @param setmealPageQueryDTO setmeal 页面查询 dto
     * @return 页面结果
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 删除批处理
     *
     * @param ids IDS
     */
    void deleteBatch(List<Long> ids);

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return Setmeal DTO
     */
    SetmealDTO selectById(Long id);

    /**
     * 更新
     *
     * @param setmealDTO Setmeal DTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 启用与否
     *
     * @param status 地位
     */
    void enableOrNot(Integer status,Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
