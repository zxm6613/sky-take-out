package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {


    /**
     * 分页
     *
     * @param name 名字
     * @param type 类型
     * @return 页面<类别>
     */
    Page<Category> page(String name, Integer type);

    /**
     * 更新
     *
     * @param category 类别
     */
    void update(Category category);

    /**
     * 插入
     *
     * @param category 类别
     */
    void insert(Category category);

    /**
     * 根据类型查询分类
     *
     * @param status 地位
     */
    List<Category> selectByType(Integer status);

    /**
     * 根据id删除分类
     *
     * @param id id
     */
    void deleteById(Long id);
}
