package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 分类实现
 *
 * @author 周简coding~~~
 * @date 2024/01/22
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 分页
     *
     * @param categoryPageQueryDTO 分类页面查询 DTO
     * @return 封装分页查询结果
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        //设置分页参数
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        //拿到名字和类型，开始进行分页操作
        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();
        Page<Category> page = categoryMapper.page(name, type);

        //在分页的集合中，拿到总记录数和数据，封装返回
        long total = page.getTotal();
        List<Category> records = page.getResult();
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        return pageResult;

    }

    /**
     * 添加类别
     *
     * @param categoryDTO 类别 DTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        //复制属性，用BeanUtils ,从源到目的
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE); //默认禁用
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId()); //在本次线程的空间中取出ID值
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }

    /**
     * 启用或禁用
     *
     * @param status 状态
     * @param id     id
     */
    @Override
    public void enableOrDisable(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 类别 DTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
//        category.setUpdateUser(BaseContext.getCurrentId());
//        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     *
     * @param type 地位
     * @return result<分类>
     */
    @Override
    public List<Category> selectByType(Integer type) {
        return categoryMapper.selectByType(type);
    }

    /**
     * 根据id删除分类
     *
     * @param id id
     */
    @Override
    public void deleteById(Long id) {
        //这里有一个注意点，套餐和分类中可能与菜品有所绑定，如果绑定就不能删除
        int count = dishMapper.selectCountByCategoryId(id);
        if (count > 0) throw new BaseException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);

        count = setmealMapper.selectCountByCategoryId(id);
        if (count > 0) throw new BaseException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);

        categoryMapper.deleteById(id);
    }
}
