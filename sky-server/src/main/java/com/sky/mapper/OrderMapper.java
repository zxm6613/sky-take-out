package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrdersPageQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     *
     * @param orders 订单
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     */
    void update(Orders orders);

    /**
     * 订单查询
     *
     * @param orders 订单
     * @return 页面<订单页面查询 vo>
     */
    Page<OrdersPageQueryVO> page(Orders orders);

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return 订单
     */
    @Select("select * from orders where id = #{id}")
    Orders selectById(Long id);

    /**
     * 管理端分页
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 页面<订单页面查询 vo>
     */
    Page<OrdersPageQueryVO> adminPage(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select COUNT(*) from orders where status = #{status}")
    Integer count(Integer status);
}
