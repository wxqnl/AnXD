package com.cug.daijiaguanli.controller;


import com.cug.daijiaguanli.enetity.system.SysUser;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.SysUserService;
import com.cug.daijiaguanli.utils.JwtUtil;
import com.cug.daijiaguanli.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j

@RestController
@RequestMapping(value="/securityLogin")
//@CrossOrigin(origins = "http://localhost:3001")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SecurityLoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("login")
    public Result login(String username, String password) {
        //System.out.println(username+" "+password);
        System.out.println("SysUserService class: " + sysUserService.getClass().getName()); // 添加这行日志
        if(username==null||password==null){
            return Result.fail("账号和密码不可为空");
        }
        SysUser logUser=sysUserService.findByUserName(username);

        //判断管理员是否存在
        if(logUser==null)
        {
            return Result.fail("管理员不存在");
        }
        //判断密码

        String passwordSecret= DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        String realpassword=logUser.getPassword();
        if(realpassword.equals(passwordSecret))
        {
            Map<String,Object> map=new HashMap<>();
            map.put("id",logUser.getId());
            map.put("username",logUser.getUsername());
            //System.out.println("username="+logUser.getNickName());
            String token= JwtUtil.genToken(map);
            //token存到redis
            ValueOperations<String,String> operations=stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.ok(token);
        }

        return Result.fail("密码错误");

    }


    @GetMapping("getUserInfo")
    public Result<SysUser> getUserInfo(@RequestHeader(name="Authorization") String token) {
        Map<String,Object>map= ThreadLocalUtil.get();
        String name=(String)map.get("username");
        SysUser user=sysUserService.findByUserName(name);
        return Result.ok(user);
    }

}