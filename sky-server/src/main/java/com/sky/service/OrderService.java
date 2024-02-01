package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersPageQueryVO;

/**
 * 订购服务
 *
 * @author 周简coding~~~
 * @date 2024/01/31
 */
public interface OrderService {
    /**
     * 提交订单
     *
     * @param ordersSubmitDTO ordersSubmitDTO
     * @return 订单提交 VO
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单查询
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 页面结果
     */
    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单明细
     *
     * @param id 编号
     * @return 订单页面查询 VO
     */
    OrdersPageQueryVO ordersDetail(Long id);

    /**
     * 继续订单
     *
     * @param id 编号
     */
    void continueOrders(Long id);

    /**
     * 取消订单
     *
     * @param id 编号
     */
    void cancelOrders(Long id);

    /**
     * 管理端分页
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 页面结果
     */
    PageResult adminPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 获取单个订单信息
     *
     * @param id 编号
     * @return 订单页面查询 VO
     */
    OrdersPageQueryVO selectById(Long id);

    /**
     * 各个状态的订单数量统计
     *
     * @return 订单统计 VO
     */
    OrderStatisticsVO count();


    /**
     * 接单
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 订单拒绝 DTO
     */
    void reject(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     *
     * @param ordersCancelDTO o
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派单
     *
     * @param id 编号
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id 编号
     */
    void complete(Long id);
}
