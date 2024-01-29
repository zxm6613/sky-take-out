package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * 商店控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/27
 */
@RestController("adminShopController") //指定bean的名称
@RequestMapping("/admin/shop")
@Api("店铺控制器")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置状态
     *
     * @param status 地位
     * @return result<对象>
     */
    @PutMapping("/{status}")
    @ApiOperation("设置状态")
    public Result<Object> setStatus(@PathVariable Integer status) {
        log.info("店铺状态为{}", status == 1 ? "营业中" : "打烊中");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(ShopController.KEY, status);
        return Result.success();
    }

    /**
     * 获取商店状态
     *
     * @return 结果<整数>
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(ShopController.KEY);
        return Result.success(status);
    }
}
