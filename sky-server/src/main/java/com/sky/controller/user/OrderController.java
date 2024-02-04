package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersPageQueryVO;
import com.sky.webSocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单提交 DTO
     * @return 结果<订单提交VO>
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单数据{}", ordersSubmitDTO);
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
        String number = ordersPaymentDTO.getOrderNumber();
        Orders ordersDB = orderMapper.getByNumber(number);

        //通过webSocket像客户端推送实时信息 type orderId content
        Map map = new HashMap();
        map.put("type", 1); //1表示来单提醒，2表示催单
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号为：" + number); //订单号
        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
        return Result.success(orderPaymentVO);
    }

    /**
     * 订单查询
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 结果<页面结果>
     */
    @GetMapping("/historyOrders")
    @ApiOperation("订单查询")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询分页参数：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.page(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单明细
     *
     * @param id 编号
     * @return 结果<订单页面查询 vo>
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("订单详情")
    public Result<OrdersPageQueryVO> ordersDetail(@PathVariable Long id) {
        log.info("要查询的订单id为：{}", id);
        OrdersPageQueryVO ordersPageQueryVO = orderService.ordersDetail(id);
        return Result.success(ordersPageQueryVO);
    }

    /**
     * 继续订单
     *
     * @param id 编号
     * @return result<对象>
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result<Object> continueOrders(@PathVariable Long id) {
        log.info("再来一单的订单id为{}", id);
        orderService.continueOrders(id);
        return Result.success();
    }

    /**
     * 取消订单
     *
     * @param id 编号
     * @return result<对象>
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result<Object> cancelOrders(@PathVariable Long id){
        log.info("取消的订单id是{}",id);
        orderService.cancelOrders(id);
        return Result.success();
    }

    /**
     * 客户催单
     *
     * @param id 编号
     * @return result<对象>
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("客户催单")
    public Result<Object> reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }
}
