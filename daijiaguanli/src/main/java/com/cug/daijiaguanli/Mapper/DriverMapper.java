package com.cug.daijiaguanli.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.driver.DriverAccount;
import com.cug.daijiaguanli.enetity.driver.DriverAccountDetail;
import com.cug.daijiaguanli.enetity.driver.DriverInfo;
import com.cug.daijiaguanli.enetity.driver.DriverLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@DS("driver")
public interface DriverMapper {
    List<DriverInfo> selectDriverInfoWithCondition(@Param("name") String name,
                                                   @Param("nickname") String nickname,
                                                   @Param("gender") String gender,
                                                   @Param("phone") String phone,
                                                   @Param("jobNo") String jobNo,
                                                   @Param("status") String status,
                                                   @Param("authStatus") String authStatus,
                                                   @Param("minScore") BigDecimal minScore,
                                                   @Param("maxScore") BigDecimal maxScore,
                                                   @Param("minOrderCount") String minOrderCount,
                                                   @Param("maxOrderCount") String maxOrderCount,
                                                   @Param("offset") int offset,
                                                   @Param("limit") int limit);

    long countDriverInfoWithCondition(@Param("name") String name,
                                      @Param("nickname") String nickname,
                                      @Param("gender") String gender,
                                      @Param("phone") String phone,
                                      @Param("jobNo") String jobNo,
                                      @Param("status") String status,
                                      @Param("authStatus") String authStatus,
                                      @Param("minScore") BigDecimal minScore,
                                      @Param("maxScore") BigDecimal maxScore,
                                      @Param("minOrderCount") String minOrderCount,
                                      @Param("maxOrderCount") String maxOrderCount);

    DriverInfo selectByName(String name);

    DriverInfo selectByJobNo(String jobNo);

    int insert(DriverInfo driverInfo);

    int update(DriverInfo driverInfo);

    int deleteByName(String name);

    DriverLoginLog selectLatestByName(String name);

    List<DriverAccountDetail> selectDriverAccountDetailWithCondition(@Param("driverId") String driverId,
                                                  @Param("tradeNo") String tradeNo,
                                                  @Param("tradeType") String tradeType,
                                                  @Param("minAmount") BigDecimal minAmount,
                                                  @Param("maxAmount") BigDecimal maxAmount,
                                                  @Param("offset") int offset,
                                                  @Param("limit") int limit);

    long countDriverAccountDetailWithCondition(@Param("driverId") String driverId,
                            @Param("tradeNo") String tradeNo,
                            @Param("tradeType") String tradeType,
                            @Param("minAmount") BigDecimal minAmount,
                            @Param("maxAmount") BigDecimal maxAmount);

    DriverAccount selectDriverAccount(String driverId);

    DriverInfo selectById(Long id);
}
