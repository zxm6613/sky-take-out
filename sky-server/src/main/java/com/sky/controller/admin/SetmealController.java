package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/26
 */
@RestController
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
    public Result<Object> insert(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐为{}",setmealDTO);
        setmealService.insert(setmealDTO);
        return Result.success();
    }
}
