package com.cug.daijiaguanli.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.coupon.CouponInfo;
import com.cug.daijiaguanli.enetity.coupon.CustomerCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
@DS("coupon")
public interface CouponMapper {

    List<CouponInfo> selectCouponInfoWithCondition(@Param("couponType") Integer couponType,
                                                   @Param("name") String name,
                                                   @Param("amount") BigDecimal amount,
                                                   @Param("minDiscount") BigDecimal minDiscount,
                                                   @Param("maxDiscount") BigDecimal maxDiscount,
                                                   @Param("minConditionAmount") BigDecimal minConditionAmount,
                                                   @Param("maxConditionAmount") BigDecimal maxConditionAmount,
                                                   @Param("status") Integer status,
                                                   @Param("minPublishCount") Integer minPublishCount,
                                                   @Param("maxPublishCount") Integer maxPublishCount,
                                                   @Param("minPerLimit") Integer minPerLimit,
                                                   @Param("maxPerLimit") Integer maxPerLimit,
                                                   @Param("minUseCount") Integer minUseCount,
                                                   @Param("maxUseCount") Integer maxUseCount,
                                                   @Param("minReceiveCount") Integer minReceiveCount,
                                                   @Param("maxReceiveCount") Integer maxReceiveCount,
                                                   @Param("minExpireTime") Date minExpireTime,
                                                   @Param("maxExpireTime") Date maxExpireTime,
                                                   @Param("offset") int offset,
                                                   @Param("limit") int limit);

    long countCouponInfoWithCondition(@Param("couponType") Integer couponType,
                                      @Param("name") String name,
                                      @Param("amount") BigDecimal amount,
                                      @Param("minDiscount") BigDecimal minDiscount,
                                      @Param("maxDiscount") BigDecimal maxDiscount,
                                      @Param("minConditionAmount") BigDecimal minConditionAmount,
                                      @Param("maxConditionAmount") BigDecimal maxConditionAmount,
                                      @Param("status") Integer status,
                                      @Param("minPublishCount") Integer minPublishCount,
                                      @Param("maxPublishCount") Integer maxPublishCount,
                                      @Param("minPerLimit") Integer minPerLimit,
                                      @Param("maxPerLimit") Integer maxPerLimit,
                                      @Param("minUseCount") Integer minUseCount,
                                      @Param("maxUseCount") Integer maxUseCount,
                                      @Param("minReceiveCount") Integer minReceiveCount,
                                      @Param("maxReceiveCount") Integer maxReceiveCount,
                                      @Param("minExpireTime") Date minExpireTime,
                                      @Param("maxExpireTime") Date maxExpireTime);

    int insertCouponInfo(CouponInfo couponInfo);

    int updateCouponInfo(CouponInfo couponInfo);

    int removeCouponInfo(Long id);

    CouponInfo selectByName(String name);

    List<CustomerCoupon> selectCustomerCouponWithCondition(@Param("customerId") Long customerId,
                                             @Param("orderId") Long orderId,
                                             @Param("status") Integer status,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    long countCustomerCouponWithCondition(@Param("customerId") Long customerId,
                            @Param("orderId") Long orderId,
                            @Param("status") Integer status);
}
