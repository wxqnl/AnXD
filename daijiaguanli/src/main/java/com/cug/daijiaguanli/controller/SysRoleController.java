package com.cug.daijiaguanli.controller;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysRole;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
//@Tag(name = "角色管理")
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    // 分页条件查询角色列表
    @GetMapping("/list")
    public Result<PageBean<SysRole>> list(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(required = false) String roleName,
                                          @RequestParam(required = false) String roleCode) {
        PageBean<SysRole> page = sysRoleService.getRoleList(pageNum, pageSize, roleName, roleCode);
        return Result.ok(page);
    }

    // 添加角色
    @PostMapping("/add")
    public Result add(@RequestBody SysRole role) {
        sysRoleService.addRole(role);
        return Result.ok();
    }

    // 更新角色
    @PostMapping("/update")
    public Result update(@RequestBody SysRole role) {
        sysRoleService.updateRole(role);
        return Result.ok();
    }

    // 删除角色
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id) {
        sysRoleService.deleteRole(id);
        return Result.ok();
    }

    // 获取单个角色信息
    @GetMapping("/getById")
    public Result<SysRole> getById(@RequestParam Long id) {
        return Result.ok(sysRoleService.getById(id));
    }


}

