package com.atguigu.daijia.mgr.controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.alibaba.fastjson.JSON;
import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.common.util.AuthContextHolder;
import com.atguigu.daijia.model.form.system.LoginForm;
import com.atguigu.daijia.system.client.SecurityLoginFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "security登录管理")
@RestController
@RequestMapping(value="/securityLogin")
//@CrossOrigin(origins = "http://localhost:3001")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SecurityLoginController {

	@Resource
	private SecurityLoginFeignClient securityLoginFeignClient;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Operation(summary = "模拟登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginForm loginForm) {
        System.out.println("loginForm1="+loginForm);
        log.info(JSON.toJSONString(loginForm));
        System.out.println("loginForm2="+loginForm);
        return Result.ok();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("getUserInfo")
    public Result<Map<String, Object>> getUserInfo()
    {
        System.out.println("getUserInfo");
        Long userId = AuthContextHolder.getUserId();
        System.out.println("userId="+userId);
        Map<String, Object> map = securityLoginFeignClient.getUserInfo(userId).getData();
        return Result.ok(map);
    }
//    @Operation(summary = "获取用户信息")
//    @GetMapping("getUserInfo")
//    public Result<Map<String, Object>> getUserInfo() {
//        try {
//            log.info("尝试获取固定用户ID=1的信息");
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", "管理员");
//            map.put("avatar", "");
//            return Result.ok(map);
//        } catch (Exception e) {
//            log.error("获取用户信息失败：", e);
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", "管理员");
//            map.put("avatar", "");
//            return Result.ok(map);
//        }
//    }
}


//package com.atguigu.daijia.mgr.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.atguigu.daijia.common.result.Result;
//import com.atguigu.daijia.common.util.AuthContextHolder;
//import com.atguigu.daijia.model.form.system.LoginForm;
//import com.atguigu.daijia.system.client.SecurityLoginFeignClient;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Tag(name = "security登录管理")
//@RestController
//@RequestMapping(value="/securityLogin")
//@SuppressWarnings({"unchecked", "rawtypes"})
//public class SecurityLoginController {
//
//    @Resource
//    private SecurityLoginFeignClient securityLoginFeignClient;
//
//    @Operation(summary = "模拟登录")
//    @PostMapping("login")
//    public Result login(@RequestBody LoginForm loginForm) {
//        log.info(JSON.toJSONString(loginForm));
//        return Result.ok();
//    }
//
//    @Operation(summary = "获取用户信息")
//    @GetMapping("getUserInfo")
//    public Result<Map<String, Object>> getUserInfo() {
//        try {
//            log.info("尝试获取固定用户ID=1的信息");
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", "管理员");
//            map.put("avatar", "");
//            return Result.ok(map);
//        } catch (Exception e) {
//            log.error("获取用户信息失败：", e);
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", "管理员");
//            map.put("avatar", "");
//            return Result.ok(map);
//        }
//    }
//}