package com.sky.service;

import com.sky.vo.*;

import java.time.LocalDate;

/**
 * 报表服务
 *
 * @author 周简coding~~~
 * @date 2024/02/03
 */
public interface ReportService {
    /**
     * 获取营业额统计
     *
     * @param begin 开始
     * @param end   结束
     * @return 营业额报告 VO
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取用户报告
     *
     * @param begin 开始
     * @param end   结束
     * @return 用户报告 VO
     */
    UserReportVO getUserReport(LocalDate begin, LocalDate end);

    /**
     * 获取订单统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return 订单统计 VO
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取 Sales Top10 报告
     *
     * @param begin 开始
     * @param end   结束
     * @return Sales Top10 报告 VO
     */
    SalesTop10ReportVO getSalesTop10Report(LocalDate begin, LocalDate end);
}
