package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Object> addDish(@RequestBody DishDTO dishDTO){
        log.info("菜品参数为{}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

}
