package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/30
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Api("添加购物车")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车 DTO
     * @return result<对象>
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<Object> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("需要添加的用户购物车信息为{}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return 结果<列表 < 购物车>>
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list() {
        List<ShoppingCart> result = shoppingCartService.list();
        return Result.success(result);
    }

    /**
     * 清理
     *
     * @return result<对象>
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<Object> clear() {
        shoppingCartService.deleteAll();
        return Result.success();
    }

    /**
     * 购物车减一
     *
     * @param shoppingCartDTO 购物车 DTO
     * @return result<对象>
     */
    @PostMapping("/sub")
    @ApiOperation("购物车减一")
    public Result<Object> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车减一{}", shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }
}
