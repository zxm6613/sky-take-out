package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单详细信息映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/31
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入
     *
     * @param orderDetailList 订单明细表
     */
    void insert(List<OrderDetail> orderDetailList);
}
