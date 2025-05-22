package com.cug.daijiaguanli.controller;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.coupon.CouponInfo;
import com.cug.daijiaguanli.enetity.coupon.CustomerCoupon;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/coupon")
public class CouponCotroller {

    @Autowired
    private CouponService couponService;

    //条件查询所有优惠卷
    @RequestMapping("/getCouponInfo")
    public Result<PageBean<CouponInfo>> getCouponInfo(Integer pageNum,
                                                      Integer pageSize,
                                                      @RequestParam(required = false) Integer couponType,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) BigDecimal amount,
                                                      @RequestParam(required = false) BigDecimal minDiscount,
                                                      @RequestParam(required = false) BigDecimal maxDiscount,
                                                      @RequestParam(required = false) BigDecimal minConditionAmount,
                                                      @RequestParam(required = false) BigDecimal maxConditionAmount,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(required = false) Integer minPublishCount,
                                                      @RequestParam(required = false) Integer maxPublishCount,
                                                      @RequestParam(required = false) Integer minPerLimit,
                                                      @RequestParam(required = false) Integer maxPerLimit,
                                                      @RequestParam(required = false) Integer minUseCount,
                                                      @RequestParam(required = false) Integer maxUseCount,
                                                      @RequestParam(required = false) Integer minReceiveCount,
                                                      @RequestParam(required = false) Integer maxReceiveCount,
                                                      @RequestParam(required = false) Date minExpireTime,
                                                      @RequestParam(required = false) Date maxExpireTime
                                                      ) {
        PageBean<CouponInfo> couponInfoPageBean = couponService.getCouponInfo(pageNum, pageSize, couponType, name, amount, minDiscount, maxDiscount, minConditionAmount, maxConditionAmount, status, minPublishCount, maxPublishCount, minPerLimit, maxPerLimit, minUseCount, maxUseCount, minReceiveCount, maxReceiveCount, minExpireTime, maxExpireTime);
        return Result.ok(couponInfoPageBean);
    }

    //更新优惠券
    @RequestMapping("/updateCouponInfo")
    public Result<CouponInfo> updateCouponInfo(CouponInfo couponInfo) {
        couponService.updateCouponInfo(couponInfo);
        return Result.ok();
    }

    //删除优惠券
    @RequestMapping("/removeCouponInfo")
    public Result<CouponInfo> removeCouponInfo(Long id) {
        couponService.removeCouponInfo(id);
        return Result.ok();
    }

    //添加优惠券
    @RequestMapping("/addCouponInfo")
    public Result<CouponInfo> addCouponInfo(CouponInfo couponInfo) {
        couponService.addCouponInfo(couponInfo);
        return Result.ok();
    }

    //获取优惠券详情
    @RequestMapping("/getCouponInfoByName")
    public Result<CouponInfo> getCouponInfoById(String name) {
        CouponInfo couponInfo = couponService.getCouponInfoByName(name);
        return Result.ok(couponInfo);
    }

    //获取用户优惠卷
    @RequestMapping("/getCustomerCouponInfoByUserId")
    public Result<PageBean<CustomerCoupon>> getCustomerCouponInfoByUserId(Integer pageNum,
                                                                          Integer pageSize,
                                                                          @RequestParam(required = false) Long customerId,
                                                                          @RequestParam(required = false) Long orderId,
                                                                          @RequestParam(required = false) Integer status)
    {
        PageBean<CustomerCoupon> customerCouponPageBean = couponService.getCustomerCouponInfoByUserId(pageNum, pageSize, customerId, orderId, status);
        return Result.ok(customerCouponPageBean);
    }




}
