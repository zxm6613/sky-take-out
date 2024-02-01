package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

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
}
