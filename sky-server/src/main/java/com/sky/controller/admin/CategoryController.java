package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 类别控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/22
 */
@RestController
@RequestMapping("/admin/category")
@Api("分类控制器")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 分页
     *
     * @param categoryPageQueryDTO 分类页面查询 DTO
     * @return result<对象>
     */
    @GetMapping("/page")
    @ApiOperation("分类：分页功能")
    public Result<Object> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类参数有{}", categoryPageQueryDTO);
        PageResult result = categoryService.page(categoryPageQueryDTO);
        return Result.success(result);
    }


    /**
     * 添加类别
     *
     * @param categoryDTO 类别 DTO
     * @return result<对象>
     */
    @PostMapping
    @ApiOperation("添加类别功能")
    public Result<Object> addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("添加的分类数据为{}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }


    /**
     * 启用或禁用
     *
     * @param status 地位
     * @param id     id
     * @return result<对象>
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用功能")
    public Result<Object> enableOrDisable(@PathVariable Integer status, Long id) {
        log.info("状态为{}，id为{}", status, id);
        categoryService.enableOrDisable(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 类别 DTO
     * @return result<对象>
     */
    @PutMapping
    @ApiOperation("修改分类功能")
    public Result<Object> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改的参数{}", categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     *
     * @param status 地位
     * @return result<分类>
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> selectByType(Integer status) {
        log.info("根据类型查询分类{}",status);
        List<Category> categories = categoryService.selectByType(status);
        return Result.success(categories);
    }

    /**
     * 根据id删除分类
     *
     * @param id id
     * @return result<对象>
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result<Object> deleteById(Long id){
        log.info("id是{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }
}
