package com.sky.controller.user;

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
@RestController("userShopController") //指定bean的名称
@RequestMapping("/user/shop")
@Api("店铺控制器")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取商店状态
     *
     * @return 结果<整数>
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(com.sky.controller.admin.ShopController.KEY);
        return Result.success(status);
    }
}
