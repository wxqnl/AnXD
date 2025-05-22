package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.order.OrderInfo;

import java.util.Date;

public interface OrderService {

    PageBean<OrderInfo> getOrderInfo(Integer pageNum, Integer pageSize, Long customerId, Long driverId, Integer status, Date minAcceptTime, Date maxAcceptTime);
    void addOrder(OrderInfo orderInfo);
    void updateOrder(OrderInfo orderInfo);
    void deleteOrder(String orderNo);
    OrderInfo getByOrderNo(String orderNo);

}
