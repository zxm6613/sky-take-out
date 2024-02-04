package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 支付超时处理
     * 在用户下单后，有15分钟支付时间，单超过15分钟，订单将会自动取消
     * 这里以一分钟监测
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void PaymentTimeout() {
        log.info("支付超时处理，当前时间为{}", LocalDateTime.now());
        //查询状态为待付款状态且下单时间超过15分钟的订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.selectByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (ordersList != null && ordersList.size() > 0) {
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单超时，自动取消");
                orderMapper.update(orders);
            });
        }
    }

    /**
     * 派单超时处理
     * 在商家点击派送后，一直处于派送状态，那么这个状态就得定时修改
     * 这里采取每天凌晨1点监测，因为这个时间处于订单低峰期，数据库压力较小
     * 由于默认一小时内送达，可以查每天0点，也就是前一个工作日
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void DeliveryTimedOut() {
        log.info("派单超时处理，当前时间为{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.selectByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (ordersList != null && ordersList.size() > 0) {
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            });
        }
    }
}
