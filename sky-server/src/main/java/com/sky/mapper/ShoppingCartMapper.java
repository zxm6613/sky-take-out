package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 购物车映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/30
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车中是否有shoppingCart数据
     *
     * @param shoppingCart 购物车
     * @return 列表<购物车>
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新
     *
     * @param cart 车
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart cart);

    /**
     * 插入
     *
     * @param shoppingCart 购物车
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            " VALUES (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime}) ")
    void insert(ShoppingCart shoppingCart);


    /**
     * 按用户ID查询
     *
     * @param userId 用户 ID
     * @return 列表<购物车>
     */
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> selectByUserId(Long userId);

    /**
     * 全部删除
     */
    @Delete("delete from shopping_cart")
    void deleteAll();

    /**
     * 删除一个
     *
     * @param cart 车
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteOne(ShoppingCart cart);
}
