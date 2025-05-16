package com.cug.daijiaguanli.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.order.OrderComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("order")
public interface OrderCommentMapper {

    void insert(OrderComment comment);

    void update(OrderComment comment);

    void delete(Long orderId);

    OrderComment selectByOrderId(Long orderId);

    List<OrderComment> selectWithCondition(@Param("customerId") Long customerId,
                                           @Param("driverId") Long driverId,
                                           @Param("rate") Integer rate,
                                           @Param("status") Integer status,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    long countWithCondition(@Param("customerId") Long customerId,
                            @Param("driverId") Long driverId,
                            @Param("rate") Integer rate,
                            @Param("status") Integer status);
}
