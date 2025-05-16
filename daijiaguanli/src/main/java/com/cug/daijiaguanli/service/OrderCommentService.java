package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.order.OrderComment;

public interface OrderCommentService {

    void addComment(OrderComment comment);
    void updateComment(OrderComment comment);
    void deleteComment(Long orderId);
    OrderComment getByOrderId(Long orderId);
    PageBean<OrderComment> list(Integer pageNum, Integer pageSize, Long customerId, Long driverId, Integer rate, Integer status);
}
