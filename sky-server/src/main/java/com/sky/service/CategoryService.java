package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;


/**
 * 逻辑层 分类
 * @author 周简coding~~~
 * @date 2024/01/22
 */
public interface CategoryService {

    /**
     * 分页
     *
     * @return 封装分页查询结果
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 添加类别
     *
     * @param categoryDTO 类别 DTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 启用或禁用
     *
     * @param status 地位
     * @param id id
     */
    void enableOrDisable(Integer status, Long id);

    /**
     * 修改分类
     *
     * @param categoryDTO 类别 DTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 根据类型查询分类
     *
     * @param status 地位
     * @return result<分类>
     */
    List<Category> selectByType(Integer status);


    /**
     * 根据id删除分类
     *
     * @param id id
     */
    void deleteById(Long id);
}
