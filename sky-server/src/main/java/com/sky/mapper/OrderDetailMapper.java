package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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


    /**
     * 按订单 ID 查询
     *
     * @param id 编号
     * @return 列表<订单明细>
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> selectByOrderId(Long id);
}
