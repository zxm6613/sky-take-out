package com.sky.controller.admin;


import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/25
 */

@RestController
@RequestMapping("admin/dish")
@Api("菜品控制器")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result<Object> addDish(@RequestBody DishDTO dishDTO) {
        log.info("菜品参数为{}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 分页
     *
     * @param dishPageQueryDTO dish 页面查询 dto
     * @return 结果<页面结果>
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页参数为{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除批处理
     *
     * @param ids IDS
     * @return result<对象>
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result<Object> deleteBatch(@RequestParam List<Long> ids) {
        log.info("删除的id有{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return 结果<菜 VO>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> selectById(@PathVariable Long id) {
        log.info("根据id查询菜品的id为{}", id);
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * 按类别 ID 查询
     *
     * @param categoryId 类别 ID
     * @return 菜
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> selectByCategoryId(Long categoryId) {
        log.info("分类的id为{}", categoryId);
        List<Dish> dish = dishService.selectByCategoryId(categoryId);
        return Result.success(dish);
    }

    /**
     * 更新菜品的数据
     *
     * @param dishDTO 菜 dto
     * @return result<对象>
     */
    @PutMapping
    @ApiOperation("修改菜品的数据")
    public Result<Object> update(@RequestBody DishDTO dishDTO) {
        log.info("菜品的数据为{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 菜是否启用
     *
     * @param status 地位
     * @return result<对象>
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售和停售")
    public Result<Object> dishEnableOrNot(@PathVariable Integer status,Long id) {
        log.info("菜品起售和停售{}{}", status,id);
        dishService.dishEnableOrNot(status,id);
        return Result.success();
    }

}
