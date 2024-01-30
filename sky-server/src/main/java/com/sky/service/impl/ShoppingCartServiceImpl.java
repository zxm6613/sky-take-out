package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现
 *
 * @author 周简coding~~~
 * @date 2024/01/30
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车 DTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车表中是否有这个数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if (list != null && list.size() > 0) {
            //如果有这个数据,给数量加一修改即可(查到的这个集合只能为0个和一个，因为根据用户id和dishid等查，要给数量加一)
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.update(cart);
        } else {
            //没有这个数据，就插入（判断是菜品购物车还是套餐购物车）
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //插入菜品
                DishVO dishVO = dishMapper.selectById(dishId);
                shoppingCart.setAmount(dishVO.getPrice());
                shoppingCart.setImage(dishVO.getImage());
                shoppingCart.setName(dishVO.getName());
            } else {
                //插入套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.selectById(setmealId);
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    /**
     * 查看购物车
     *
     * @return 列表<购物车>
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.selectByUserId(userId);
    }

    /**
     * 全部删除
     */
    @Override
    public void deleteAll() {
        shoppingCartMapper.deleteAll();
    }

    /**
     * 购物车减一
     *
     * @param shoppingCartDTO 购物车 DTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        //查数量，数量大于1就减一，否则删除这条数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        ShoppingCart cart = shoppingCarts.get(0);
        Integer count = cart.getNumber();
        if (count > 1) {
            //数量减一
            cart.setNumber(count - 1);
            shoppingCartMapper.update(cart);
        } else {
            //删除
            shoppingCartMapper.deleteOne(cart);
        }
    }
}
