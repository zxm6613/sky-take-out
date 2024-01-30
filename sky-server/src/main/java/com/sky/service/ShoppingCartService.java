package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * 购物车服务
 *
 * @author 周简coding~~~
 * @date 2024/01/30
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车 DTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     *
     * @return 列表<购物车>
     */
    List<ShoppingCart> list();

    /**
     * 全部删除
     */
    void deleteAll();


    /**
     * 购物车减一
     *
     * @param shoppingCartDTO 购物车 DTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
