package com.cug.daijiaguanli.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.customer.CustomerInfo;
import com.cug.daijiaguanli.enetity.customer.CustomerLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("customer")
public interface CustomerMapper {
    CustomerInfo selectByUsername(@Param("username") String username);

    CustomerInfo selectById(@Param("id") Long id);

    int deleteByUsername(@Param("username") String username);

    int updateCustomerInfo(CustomerInfo customerInfo);

    List<CustomerInfo> selectAllWithCondition(@Param("nickname") String nickname,
                                              @Param("gender") Byte gender,
                                              @Param("phone") String phone,
                                              @Param("status") String status,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);

    Long countAllWithCondition(@Param("nickname") String nickname,
                               @Param("gender") Byte gender,
                               @Param("phone") String phone,
                               @Param("status") String status);

    CustomerLoginLog selectLatestLogByCustomerId(@Param("customerId") Long customerId);
}
