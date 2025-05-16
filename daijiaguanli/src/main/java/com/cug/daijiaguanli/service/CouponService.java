package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.coupon.CouponInfo;
import com.cug.daijiaguanli.enetity.coupon.CustomerCoupon;

import java.math.BigDecimal;
import java.util.Date;

public interface CouponService {
    PageBean<CouponInfo> getCouponInfo(Integer pageNum, Integer pageSize,
                                       Integer couponType, String name, BigDecimal amount,
                                       BigDecimal minDiscount, BigDecimal maxDiscount,
                                       BigDecimal minConditionAmount, BigDecimal maxConditionAmount,
                                       Integer status, Integer minPublishCount, Integer maxPublishCount,
                                       Integer minPerLimit, Integer maxPerLimit,
                                       Integer minUseCount, Integer maxUseCount,
                                       Integer minReceiveCount, Integer maxReceiveCount,
                                       Date minExpireTime, Date maxExpireTime);

    void updateCouponInfo(CouponInfo couponInfo);

    void removeCouponInfo(Long id);

    void addCouponInfo(CouponInfo couponInfo);

    CouponInfo getCouponInfoByName(String name);

    PageBean<CustomerCoupon> getCustomerCouponInfoByUserId(Integer pageNum, Integer pageSize,
                                                           Long customerId, Long orderId, Integer status);
}
