package com.cug.daijiaguanli.service.Impl;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.coupon.CouponInfo;
import com.cug.daijiaguanli.enetity.coupon.CustomerCoupon;
import com.cug.daijiaguanli.mapper.CouponMapper;
import com.cug.daijiaguanli.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public PageBean<CouponInfo> getCouponInfo(Integer pageNum, Integer pageSize,
                                              Integer couponType, String name, BigDecimal amount,
                                              BigDecimal minDiscount, BigDecimal maxDiscount,
                                              BigDecimal minConditionAmount, BigDecimal maxConditionAmount,
                                              Integer status, Integer minPublishCount, Integer maxPublishCount,
                                              Integer minPerLimit, Integer maxPerLimit,
                                              Integer minUseCount, Integer maxUseCount,
                                              Integer minReceiveCount, Integer maxReceiveCount,
                                              Date minExpireTime, Date maxExpireTime) {
        int offset = (pageNum - 1) * pageSize;
        List<CouponInfo> list = couponMapper.selectCouponInfoWithCondition(
                couponType, name, amount, minDiscount, maxDiscount, minConditionAmount, maxConditionAmount,
                status, minPublishCount, maxPublishCount, minPerLimit, maxPerLimit, minUseCount, maxUseCount,
                minReceiveCount, maxReceiveCount, minExpireTime, maxExpireTime, offset, pageSize
        );
        long count = couponMapper.countCouponInfoWithCondition(
                couponType, name, amount, minDiscount, maxDiscount, minConditionAmount, maxConditionAmount,
                status, minPublishCount, maxPublishCount, minPerLimit, maxPerLimit, minUseCount, maxUseCount,
                minReceiveCount, maxReceiveCount, minExpireTime, maxExpireTime
        );
        return new PageBean<>(count, list);
    }

    @Override
    public void updateCouponInfo(CouponInfo couponInfo) {
        couponMapper.updateCouponInfo(couponInfo);
    }

    @Override
    public void removeCouponInfo(Long id) {
        couponMapper.removeCouponInfo(id);
    }

    @Override
    public void addCouponInfo(CouponInfo couponInfo) {
        couponMapper.insertCouponInfo(couponInfo);
    }

    @Override
    public CouponInfo getCouponInfoByName(String name) {
        return couponMapper.selectByName(name);
    }

    @Override
    public PageBean<CustomerCoupon> getCustomerCouponInfoByUserId(Integer pageNum, Integer pageSize,
                                                                  Long customerId, Long orderId, Integer status) {
        int offset = (pageNum - 1) * pageSize;
        List<CustomerCoupon> list = couponMapper.selectCustomerCouponWithCondition(customerId, orderId, status, offset, pageSize);
        long count = couponMapper.countCustomerCouponWithCondition(customerId, orderId, status);
        return new PageBean<>(count, list);
    }

}
