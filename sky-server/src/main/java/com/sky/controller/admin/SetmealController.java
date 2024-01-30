package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/26
 */
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api("套餐控制器")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 插入套餐
     *
     * @param setmealDTO Setmeal DTO
     * @return result<对象>
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.id")
    public Result<Object> insert(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐为{}", setmealDTO);
        setmealService.insert(setmealDTO);
        return Result.success();
    }

    /**
     * 分页
     *
     * @param setmealPageQueryDTO setmeal 页面查询 dto
     * @return 结果<页面结果>
     */
    @GetMapping("/page")
    @ApiOperation("分页显示")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页数据为{}", setmealPageQueryDTO);
        PageResult result = setmealService.page(setmealPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 删除批处理
     *
     * @param ids IDS
     * @return result<对象>
     */
    @DeleteMapping
    @ApiOperation("批量删除")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result<Object> deleteBatch(@RequestParam List<Long> ids){
        log.info("批量删除id为{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return 结果<Setmeal DTO>
     */
    @GetMapping("/{id}")
    @ApiOperation("回显数据")
    public Result<SetmealDTO> selectById(@PathVariable Long id){
        log.info("回显的id是{}",id);
        SetmealDTO setmealDTO = setmealService.selectById(id);
        return Result.success(setmealDTO);
    }

    /**
     * 更新
     *
     * @param setmealDTO Setmeal DTO
     * @return result<对象>
     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.id")
    public Result<Object> update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐的数据是{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 启用与否
     *
     * @param status 地位
     * @return result<对象>
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用功能")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result<Object> enableOrNot(@PathVariable Integer status,Long id){
        log.info("当前状态为{}",status);
        setmealService.enableOrNot(status,id);
        return Result.success();
    }
}
