package com.cug.daijiaguanli.controller;


import com.cug.daijiaguanli.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/getFeed")
    public Result<String> getFeed(Long orderId) {
        if (orderId == null) {
            return Result.fail("订单ID不能为空");
        }
        String key = orderId +"_feedback";
        String feed = stringRedisTemplate.opsForValue().get(key);
        return Result.ok(feed);
    }

}
