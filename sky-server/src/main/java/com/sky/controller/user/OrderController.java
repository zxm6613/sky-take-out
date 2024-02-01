package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/31
 */
@RestController("userOrderController")
@RequestMapping("user/order")
@Api("用户订单控制器")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 提交
     *
     * @param ordersSubmitDTO 订单提交 DTO
     * @return 结果<订单提交VO>
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单数据{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单付款 DTO
     * @return 结果<订单付款vo>
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);

//        log.info("生成预支付交易单：{}", orderPaymentVO);

        //模拟交易成功，修改订单状态
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber()); //订单号
        return Result.success(orderPaymentVO);
    }
}
