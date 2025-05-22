package com.cug.daijiaguanli.service.Impl;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.order.OrderComment;
import com.cug.daijiaguanli.mapper.OrderCommentMapper;
import com.cug.daijiaguanli.service.OrderCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderCommentServiceImpl implements OrderCommentService {

    @Autowired
    private OrderCommentMapper orderCommentMapper;

    @Override
    public void addComment(OrderComment comment) {
        orderCommentMapper.insert(comment);
    }

    @Override
    public void updateComment(OrderComment comment) {
        orderCommentMapper.update(comment);
    }

    @Override
    public void deleteComment(Long orderId) {
        orderCommentMapper.delete(orderId);
    }

    @Override
    public OrderComment getByOrderId(Long orderId) {
        return orderCommentMapper.selectByOrderId(orderId);
    }

    @Override
    public PageBean<OrderComment> list(Integer pageNum, Integer pageSize, Long customerId, Long driverId, Integer rate, Integer status) {
        int offset = (pageNum - 1) * pageSize;
        List<OrderComment> list = orderCommentMapper.selectWithCondition(customerId, driverId, rate, status, offset, pageSize);
        long count = orderCommentMapper.countWithCondition(customerId, driverId, rate, status);
        return new PageBean<>(count, list);
    }

}
