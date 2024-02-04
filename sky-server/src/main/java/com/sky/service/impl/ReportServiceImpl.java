package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报表服务impl
 *
 * @author 周简coding~~~
 * @date 2024/02/03
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取营业额统计
     *
     * @param begin 开始
     * @param end   结束
     * @return 营业额报告 VO
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //先构建日期集合
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        LocalDate time = begin.plusDays(1);
        while (!time.equals(end)) {
            time = time.plusDays(1);
            dateList.add(time);
        }

        LocalDateTime beginTime;
        LocalDateTime endTime;
        List<Double> amountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //计算这一天最小时间和最大时间
            beginTime = LocalDateTime.of(date, LocalTime.MIN);
            endTime = LocalDateTime.of(date, LocalTime.MAX);

            //构建查询时需要映射的map
            Map<String, Object> map = new HashMap<>();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);

            //查询当天总金额
            Double amount = orderMapper.sumByMap(map);
            amount = amount == null ? 0.0 : amount; //不能让营业额为null，应该为0
            amountList.add(amount);
        }
        //构建营业额列表
//        List<Double> amountList = new ArrayList<>();
//        dateList.forEach(date -> {
//            //计算这一天最小时间和最大时间
//             beginTime = LocalDateTime.of(date, LocalTime.MIN);
//             endTime = LocalDateTime.of(date, LocalTime.MAX);
//
//            //构建查询时需要映射的map
//            Map<String, Object> map = new HashMap<>();
//            map.put("status", Orders.COMPLETED);
//            map.put("begin", beginTime);
//            map.put("end", endTime);
//
//            //查询当天总金额
//            Double amount = orderMapper.sumByMap(map);
//            amount = amount == null ? 0.0 : amount; //不能让营业额为null，应该为0
//            amountList.add(amount);
//        });

        //构建需要的字符串返回
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(amountList, ","))
                .build();
    }

    /**
     * 获取用户报告
     *
     * @param begin 开始
     * @param end   结束
     * @return 用户报告 VO
     */
    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        //先构建日期集合
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        LocalDate time = begin.plusDays(1);
        while (!time.equals(end)) {
            time = time.plusDays(1);
            dateList.add(time);
        }

        //构建新增用户和总用户集合
        List<Integer> newUsers = new ArrayList<>();
        List<Integer> allUsers = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map<String, Object> map = new HashMap<>();
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer allUser = userMapper.sumByMap(map);
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            Integer newUser = userMapper.sumByMap(map);
            allUsers.add(allUser);
            newUsers.add(newUser);
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(allUsers, ","))
                .newUserList(StringUtils.join(newUsers, ","))
                .build();
    }

    /**
     * 获取订单统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return 订单统计 VO
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        //先构建日期集合
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        LocalDate time = begin.plusDays(1);
        while (!time.equals(end)) {
            time = time.plusDays(1);
            dateList.add(time);
        }

        List<Integer> orderCounts = new ArrayList<>();
        List<Integer> effectiveOrderCounts = new ArrayList<>();
        //查看订单总数列表和订单完成率总数列表（有效订单数列表）
        for (LocalDate date : dateList) {
            Map<String, Object> map = new HashMap<>();
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer orderCount = orderMapper.countByMap(map);

            map.put("status", Orders.COMPLETED);
            Integer effectiveOrderCount = orderMapper.countByMap(map);

            orderCounts.add(orderCount);
            effectiveOrderCounts.add(effectiveOrderCount);
        }

        //计算查看订单总数和订单完成率总数
        int orderNumber = orderCounts.stream().reduce(Integer::sum).get();
        Integer effectiveOrder = effectiveOrderCounts.stream().reduce(Integer::sum).get();
        double d = 0.0;
        if (orderNumber != 0) {
            d = effectiveOrder.doubleValue() / orderNumber;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCompletionRate(d)
                .orderCountList(StringUtils.join(orderCounts, ","))
                .totalOrderCount(orderNumber)
                .validOrderCount(effectiveOrder)
                .validOrderCountList(StringUtils.join(effectiveOrderCounts, ","))
                .build();
    }

    /**
     * 获取 Sales Top10 报告
     *
     * @param begin 开始
     * @param end   结束
     * @return Sales Top10 报告 VO
     */
    @Override
    public SalesTop10ReportVO getSalesTop10Report(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");

        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        //封装返回结果数据
        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }
}
