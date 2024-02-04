package com.sky.controller.admin;

import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 报表控制器
 *
 * @author 周简coding~~~
 * @date 2024/02/03
 */
@RestController
@RequestMapping("/admin/report")
@Api("统计报表")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取营业额统计
     *
     * @param begin 开始
     * @param end   结束
     * @return 结果<营业额报告 VO>
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("统计每天总金额")
    public Result<TurnoverReportVO> getTurnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("统计每天总金额{}<--->{}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 获取用户报告
     * 统计用户
     *
     * @param begin 开始
     * @param end   结束
     * @return 结果<用户报告 VO>
     */
    @GetMapping("/userStatistics")
    @ApiOperation("统计用户")
    public Result<UserReportVO> getUserReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("统计用户请求时间{}<--->{}", begin, end);
        UserReportVO userReportVO = reportService.getUserReport(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * 获取订单统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return 结果<订单统计 vo>
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> getOrderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计{}<----->{}", begin, end);
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 获取 Sales Top10 报告
     *
     * @param begin 开始
     * @param end   结束
     * @return 结果<销售 Top10 报告 VO>
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名")
    public Result<SalesTop10ReportVO> getSalesTop10Report(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("销量排名{}<----->{}", begin, end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10Report(begin,end);
        return Result.success(salesTop10ReportVO);
    }
}
