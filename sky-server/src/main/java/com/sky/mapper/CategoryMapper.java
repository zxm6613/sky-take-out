package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 类别映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
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
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 插入
     *
     * @param category 类别
     */
    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    /**
     * 根据类型查询分类
     *
     * @param type 地位
     */
    List<Category> selectByType(Integer type);

    /**
     * 根据id删除分类
     *
     * @param id id
     */
    void deleteById(Long id);
}
