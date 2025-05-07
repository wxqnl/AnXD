package com.cug.daijiaguanli.controller;


import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.driver.DriverAccountDetail;
import com.cug.daijiaguanli.enetity.system.SysLoginLog;
import com.cug.daijiaguanli.enetity.system.SysOperLog;
import com.cug.daijiaguanli.enetity.system.SysUser;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


//@Tag(name = "用户管理")
@RestController
@RequestMapping("/sysUser")
@CrossOrigin
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    // 查询所有用户
    @GetMapping("/SearchAllUser")
    public Result<PageBean<SysUser>> SearchAllUser( Integer pageNum,
                                           Integer pageSize,
                                           @RequestParam(required = false) String id,
                                           @RequestParam(required = false) Byte username,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String phone,
                                           @RequestParam(required = false) String deptId,
                                           @RequestParam(required = false) String postId,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(required = false) String postName,
                                           @RequestParam(required = false) String deptName
                                           )
    {
        PageBean<SysUser>user=sysUserService.SearchAllUser(pageNum, pageSize, id, username, name, phone, deptId, postId, status, postName, deptName);
        return Result.ok(user);
    }

    // 根据 ID 查询用户
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.ok(sysUserService.getById(id));
    }

    // 创建用户
    @PostMapping
    public boolean create(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser);
    }

    // 更新用户
    @PutMapping
    public boolean update(@RequestBody SysUser sysUser) {
        return sysUserService.updateById(sysUser);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return sysUserService.removeById(id);
    }

    // 根据用户名查询
    @GetMapping("/findByUserName/{username}")
    public SysUser findByUserName(@PathVariable String username) {
        return sysUserService.findByUserName(username);
    }

    //获取用户日志
    @PostMapping("/getUserLoginLog")
    public Result<PageBean<SysLoginLog>>getUserLoginLog(Integer pageNum,
                                                        Integer pageSize,
                                                        @RequestParam(required = false) String username,
                                                        @RequestParam(required = false) Date minAccessTime,
                                                        @RequestParam(required = false) Date maxAccessTime
                                                        )
    {
        PageBean<SysLoginLog> pg=sysUserService.getUserLoginLog(pageNum,  pageSize, username, minAccessTime, maxAccessTime);
        return Result.ok(pg);
    }

    //获取用户操作日志
    @PostMapping("/getUserOperLog")
    public Result<PageBean<SysOperLog>>getUserOperLog(Integer pageNum,
                                                      Integer pageSize,
                                                      @RequestParam(required = false) String operName,
                                                      @RequestParam(required = false) String deptName,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(required = false) String operatorType,
                                                      @RequestParam(required = false) String businessType,
                                                      @RequestParam(required = false) String requestMethod,
                                                      @RequestParam(required = false) Date minOperTime,
                                                      @RequestParam(required = false) Date maxOperTime
                                                      )
    {
        PageBean<SysOperLog> pg=sysUserService.getUserOperLog(pageNum,  pageSize, operName, deptName, status, operatorType, businessType, requestMethod, minOperTime, maxOperTime);
        return Result.ok(pg);
    }


}

