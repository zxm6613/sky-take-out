package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersPageQueryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订购服务 实现
 *
 * @author 周简coding~~~
 * @date 2024/01/31
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;


    /**
     * 提交订单
     *
     * @param ordersSubmitDTO ordersSubmitDTO
     * @return 订单提交 VO
     */
    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //一---先判断异常情况 1-1:地址为空
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //1-2: 购物车为空
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserId(userId);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //二---封装订单数据，插入订单数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setStatus(Orders.PENDING_PAYMENT); //待付款
        StringBuilder address = new StringBuilder(addressBook.getProvinceName()  //拼接地址
                + addressBook.getCityName()
                + addressBook.getDistrictName()
                + addressBook.getDetail()
                + System.lineSeparator()); //这个方法会返回当前操作系统的特定换行符
        String consignee = addressBook.getConsignee();
        for (int i = consignee.length() - 1; i > 0; i--) {
            address.append('*');
        }
        address.append(consignee.charAt(0)).append("(");
        address.append(addressBook.getSex().equals(Orders.WOMEN) ? Orders.LADY : Orders.GENTLEMEN)
                .append(")")
                .append(" ")
                .append(addressBook.getPhone(), 0, 3)
                .append("****")
                .append(addressBook.getPhone().substring(7));
        orders.setAddress(address.toString()); //设置地址

        orders.setOrderTime(LocalDateTime.now()); //下单为当前时间
        orders.setAddressBookId(addressBookId); //地址簿id
        orders.setPhone(addressBook.getPhone()); //手机号
        orders.setConsignee(addressBook.getConsignee()); //收货人
        orders.setNumber(String.valueOf(System.currentTimeMillis())); //订单号为当前时间戳
        orders.setUserId(userId); //当前用户id
        orders.setPayMethod(1); //微信支付
        orders.setPayStatus(Orders.UN_PAID); //未支付
        User user = userMapper.selectById(userId);
        orders.setUserName(user.getName()); //用户名
        orderMapper.insert(orders);

        //三---插入订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCarts.forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId()); //这里的订单id得上一步插入操作的主键回显
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.insert(orderDetailList);

        //四---清空购物车
        shoppingCartMapper.deleteAll();

        //五---封装OrderSubmitVO，并返回
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );

        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 订单查询
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 页面结果
     */
    @Override
    public PageResult page(OrdersPageQueryDTO ordersPageQueryDTO) {
        //先做分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //查找当前用户的历史订单
        Orders orders = Orders.builder()
                .userId(BaseContext.getCurrentId())
                .status(ordersPageQueryDTO.getStatus())
                .build();
        //查找分页的数据
        Page<OrdersPageQueryVO> page = orderMapper.page(orders);

        //封装每一个订单的订单明细
        page.forEach(ordersPageQueryVO -> {
            List<OrderDetail> orderDetailList
                    = orderDetailMapper.selectByOrderId(ordersPageQueryVO.getId());
            ordersPageQueryVO.setOrderDetailList(orderDetailList);
        });

        //封装数据返回
        List<OrdersPageQueryVO> result = page.getResult();
        long total = page.getTotal();
        return new PageResult(total, result);
    }

    /**
     * 管理端分页
     *
     * @param ordersPageQueryDTO 订单页面查询 DTO
     * @return 页面结果
     */
    @Override
    public PageResult adminPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        //先做分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        //查找分页的数据
        Page<OrdersPageQueryVO> page = orderMapper.adminPage(ordersPageQueryDTO);

        //封装每一个订单的菜品名称
        page.forEach(ordersPageQueryVO -> {
            StringBuilder dishName = new StringBuilder();
            List<OrderDetail> orderDetailList
                    = orderDetailMapper.selectByOrderId(ordersPageQueryVO.getId());
            orderDetailList.forEach(orderDetail -> dishName.append(orderDetail.getName()).append(" "));
            ordersPageQueryVO.setOrderDishes(dishName.toString());
        });

        //封装数据返回
        List<OrdersPageQueryVO> result = page.getResult();
        long total = page.getTotal();
        return new PageResult(total, result);
    }

    /**
     * 订单明细
     *
     * @param id 编号
     * @return 订单页面查询 VO
     */
    @Override
    public OrdersPageQueryVO ordersDetail(Long id) {
        //查询订单数据
        Orders orders = orderMapper.selectById(id);

        //查询订单明细数据
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(id);

        //封装返回
        OrdersPageQueryVO ordersPageQueryVO = new OrdersPageQueryVO();
        BeanUtils.copyProperties(orders, ordersPageQueryVO);
        ordersPageQueryVO.setOrderDetailList(orderDetailList);
        return ordersPageQueryVO;
    }

    /**
     * 继续订单
     *
     * @param id 编号
     */
    @Override
    public void continueOrders(Long id) {
        //先通过订单id查到订单明细
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(id);

        //再用订单明细构造ShoppingCartDTO,进行购物车插入
        orderDetailList.forEach(orderDetail -> {
            ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
            shoppingCartDTO.setDishId(orderDetail.getDishId());
            shoppingCartDTO.setSetmealId(orderDetail.getSetmealId());
            shoppingCartDTO.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCartService.add(shoppingCartDTO);
        });
    }

    /**
     * 取消订单
     *
     * @param id 编号
     */
    @Override
    public void cancelOrders(Long id) {
        //构建orders对象，进行修改操作
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.CANCELLED)
                .build();
        orderMapper.update(orders);
    }



    /**
     * 获取单个订单信息
     *
     * @param id 编号
     * @return 订单页面查询 VO
     */
    @Override
    public OrdersPageQueryVO selectById(Long id) {
        //先查出单个数据
        Orders orders = orderMapper.selectById(id);

        //再封装数据到OrdersPageQueryVO
        OrdersPageQueryVO ordersPageQueryVO = new OrdersPageQueryVO();
        BeanUtils.copyProperties(orders,ordersPageQueryVO);
        List<OrderDetail> orderDetailList
                = orderDetailMapper.selectByOrderId(ordersPageQueryVO.getId());
        ordersPageQueryVO.setOrderDetailList(orderDetailList);
        StringBuilder dishName = new StringBuilder();
        orderDetailList.forEach(orderDetail -> dishName.append(orderDetail.getName()).append(" "));
        ordersPageQueryVO.setOrderDishes(dishName.toString());

        return ordersPageQueryVO;
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return 订单统计 VO
     */
    @Override
    public OrderStatisticsVO count() {
        //计算待接单数量
        Integer toBeConfirmed = orderMapper.count(Orders.TO_BE_CONFIRMED);
        //计算待派送数量
        Integer confirmed = orderMapper.count(Orders.CONFIRMED);
        //计算派送中数量
        Integer deliveryInProgress = orderMapper.count(Orders.DELIVERY_IN_PROGRESS);

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return orderStatisticsVO;
    }

    /**
     * 接单
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 订单拒绝 DTO
     */
    @Override
    public void reject(OrdersRejectionDTO ordersRejectionDTO) {
        Orders orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .payStatus(Orders.REFUND) //拒单退款
                .status(Orders.BACK_MONEY)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO o
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .cancelReason(ordersCancelDTO.getCancelReason())
                .status(Orders.CANCELLED)
                .cancelTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
    }

    /**
     * 派单
     *
     * @param id 编号
     */
    @Override
    public void delivery(Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.update(orders);
    }


    /**
     * 完成订单
     *
     * @param id 编号
     */
    @Override
    public void complete(Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .build();
        orderMapper.update(orders);
    }


}
