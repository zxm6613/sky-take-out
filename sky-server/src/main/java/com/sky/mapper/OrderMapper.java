package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrdersPageQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 计数
     *
     * @param status 地位
     * @return 整数
     */
    @Select("select COUNT(*) from orders where status = #{status}")
    Integer count(Integer status);

    /**
     * 按状态和订单时间选择 LT
     *
     * @param status 地位
     * @param time   时间
     * @return 列表<订单>
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> selectByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    /**
     * 按时间选择
     *
     * @param map 地图
     * @return 双
     */
    Double sumByMap(Map<String, Object> map);

    /**
     * 用map查订单数量
     *
     * @param map 地图
     * @return 整数
     */
    Integer countByMap(Map<String, Object> map);

    /**
     * 获得 Top10 销售
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 列表<商品销售DTO>
     */
//    @Select("select od.name,sum(od.number)" +
//            " from order_detail od ,orders o " +
//            "where od.order_id = o.id and o.status = 5 " +
//            "and o.order_time > #{beginTime} and o.order_time < #{endTime} group by od.name")
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
