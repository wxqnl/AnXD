package com.cug.daijiaguanli.controller;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.order.OrderInfo;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value="/order")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoController {

    @Autowired
    private OrderService orderService;

    // 条件分页查询订单
    @PostMapping("/getOrderInfo")
    public Result<PageBean<OrderInfo>> getOrderInfo(Integer pageNum,
                                                    Integer pageSize,
                                                    @RequestParam(required = false) Long customerId,
                                                    @RequestParam(required = false) Long driverId,
                                                    @RequestParam(required = false) Integer status,
                                                    @RequestParam(required = false) Date minAcceptTime,
                                                    @RequestParam(required = false) Date maxAcceptTime) {
        PageBean<OrderInfo> page = orderService.getOrderInfo(pageNum, pageSize, customerId, driverId, status, minAcceptTime, maxAcceptTime);
        return Result.ok(page);
    }

    // 添加订单
    @PostMapping("/add")
    public Result addOrder(@RequestBody OrderInfo orderInfo) {
        orderService.addOrder(orderInfo);
        return Result.ok();
    }

    // 更新订单
    @PostMapping("/update")
    public Result updateOrder(@RequestBody OrderInfo orderInfo) {
        orderService.updateOrder(orderInfo);
        return Result.ok();
    }

    // 删除订单
    @PostMapping("/delete")
    public Result deleteOrder(@RequestParam String orderNo) {
        orderService.deleteOrder(orderNo);
        return Result.ok();
    }

    // 根据订单号查询
    @PostMapping("/getByOrderNo")
    public Result<OrderInfo> getOrderByOrderNo(@RequestParam String orderNo) {
        return Result.ok(orderService.getByOrderNo(orderNo));
    }


}

