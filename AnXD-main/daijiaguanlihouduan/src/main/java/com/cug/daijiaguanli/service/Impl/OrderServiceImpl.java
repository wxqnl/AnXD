package com.cug.daijiaguanli.service.Impl;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.customer.CustomerInfo;
import com.cug.daijiaguanli.enetity.driver.DriverInfo;
import com.cug.daijiaguanli.enetity.order.OrderInfo;
import com.cug.daijiaguanli.mapper.CustomerMapper;
import com.cug.daijiaguanli.mapper.DriverMapper;
import com.cug.daijiaguanli.mapper.OrderMapper;
import com.cug.daijiaguanli.service.DriverService;
import com.cug.daijiaguanli.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private DriverMapper driverMapper;


    @Override
    public PageBean<OrderInfo> getOrderInfo(Integer pageNum, Integer pageSize,
                                            Long customerId, Long driverId, Integer status,
                                            Date minAcceptTime, Date maxAcceptTime) {
        int offset = (pageNum - 1) * pageSize;
        List<OrderInfo> list = orderMapper.selectOrderInfoWithCondition(customerId, driverId, status, minAcceptTime, maxAcceptTime, offset, pageSize);
        for (OrderInfo orderInfo : list){
            Long id=orderInfo.getCustomerId();
            CustomerInfo cus=customerMapper.selectById(id);
            orderInfo.setCustomerName(cus.getNickname());
            Long dId=orderInfo.getDriverId();
            DriverInfo df=driverMapper.selectById(id);
            orderInfo.setDriverName(df.getNickname());
        }
        long count = orderMapper.countOrderInfoWithCondition(customerId, driverId, status, minAcceptTime, maxAcceptTime);
        return new PageBean<>(count, list);
    }

    @Override
    public void addOrder(OrderInfo orderInfo) {
        orderMapper.insertOrder(orderInfo);
    }

    @Override
    public void updateOrder(OrderInfo orderInfo) {
        orderMapper.updateOrder(orderInfo);
    }

    @Override
    public void deleteOrder(String orderNo) {
        orderMapper.deleteOrder(orderNo);
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }


}
