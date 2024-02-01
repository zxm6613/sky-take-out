package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrdersPageQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api("管理端订单模块")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 管理端条件分页（和用户端的分页不一样，用户端只需要查自己的订单，而管理端得查到所有用户的订单）
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 结果<页面结果>
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("条件分页")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("管理端分页的参数为{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.adminPage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取单个订单信息
     *
     * @param id 编号
     * @return 结果<订单页面查询 vo>
     */
    @GetMapping("/details/{id}")
    @ApiOperation("根据id回显")
    public Result<OrdersPageQueryVO> getOrder(@PathVariable Long id) {
        log.info("需要回显的id为{}", id);
        OrdersPageQueryVO ordersPageQueryVO = orderService.selectById(id);
        return Result.success(ordersPageQueryVO);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return 结果<OrderStatisticsVO>
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> count() {
        OrderStatisticsVO orderStatisticsVO = orderService.count();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 接单
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<Object> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单的信息为{}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 订单拒绝 DTO
     * @return result<对象>
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<Object> reject(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单信息为{}", ordersRejectionDTO);
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO o
     * @return result<对象>
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<Object> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单信息为{}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派单
     *
     * @param id id
     * @return result<对象>
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派单")
    public Result<Object> deliver(@PathVariable Long id) {
        log.info("派单信息为{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id 编号
     * @return result<对象>
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result<Object> complete(@PathVariable Long id) {
        log.info("完成订单为{}",id);
        orderService.complete(id);
        return Result.success();
    }
}
