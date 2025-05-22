package com.cug.daijiaguanli.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
@DS("order")
public interface OrderMapper {

    List<OrderInfo> selectOrderInfoWithCondition(@Param("customerId") Long customerId,
                                                 @Param("driverId") Long driverId,
                                                 @Param("status") Integer status,
                                                 @Param("minAcceptTime") Date minAcceptTime,
                                                 @Param("maxAcceptTime") Date maxAcceptTime,
                                                 @Param("offset") int offset,
                                                 @Param("limit") int limit);

    long countOrderInfoWithCondition(@Param("customerId") Long customerId,
                                     @Param("driverId") Long driverId,
                                     @Param("status") Integer status,
                                     @Param("minAcceptTime") Date minAcceptTime,
                                     @Param("maxAcceptTime") Date maxAcceptTime);

    int insertOrder(OrderInfo orderInfo);

    int updateOrder(OrderInfo orderInfo);

    int deleteOrder(String orderNo);

    OrderInfo selectByOrderNo(String orderNo);

}
