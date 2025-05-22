package com.cug.daijiaguanli.controller;


import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.order.OrderComment;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.OrderCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderComment")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderCommentController {

    @Autowired
    private OrderCommentService orderCommentService;

    // 添加评论
    @PostMapping("/add")
    public Result addComment(@RequestBody OrderComment orderComment) {
        orderCommentService.addComment(orderComment);
        return Result.ok();
    }

    // 修改评论
    @PostMapping("/update")
    public Result updateComment(@RequestBody OrderComment orderComment) {
        orderCommentService.updateComment(orderComment);
        return Result.ok();
    }

    // 删除评论
    @PostMapping("/delete")
    public Result deleteComment(@RequestParam Long orderId) {
        orderCommentService.deleteComment(orderId);
        return Result.ok();
    }

    // 根据订单ID查询评论
    @GetMapping("/getByOrderId")
    public Result<OrderComment> getByOrderId(@RequestParam Long orderId) {
        return Result.ok(orderCommentService.getByOrderId(orderId));
    }

    // 分页条件查询
    @PostMapping("/list")
    public Result<PageBean<OrderComment>> list(@RequestParam Integer pageNum,
                                               @RequestParam Integer pageSize,
                                               @RequestParam(required = false) Long customerId,
                                               @RequestParam(required = false) Long driverId,
                                               @RequestParam(required = false) Integer rate,
                                               @RequestParam(required = false) Integer status) {
        PageBean<OrderComment> pageBean = orderCommentService.list(pageNum, pageSize, customerId, driverId, rate, status);
        return Result.ok(pageBean);
    }
}
