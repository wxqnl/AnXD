package com.cug.daijiaguanli.controller;

import com.cug.daijiaguanli.enetity.system.SysMenu;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    // 获取菜单树结构
    @GetMapping("/list")
    public Result<List<SysMenu>> list() {
        return Result.ok(sysMenuService.getMenuTree());
    }

    // 添加菜单
    @PostMapping("/add")
    public Result add(@RequestBody SysMenu menu) {
        sysMenuService.addMenu(menu);
        return Result.ok();
    }

    // 更新菜单
    @PostMapping("/update")
    public Result update(@RequestBody SysMenu menu) {
        sysMenuService.updateMenu(menu);
        return Result.ok();
    }

    // 删除菜单
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id) {
        sysMenuService.deleteMenu(id);
        return Result.ok();
    }

    // 查询单个菜单
    @GetMapping("/getById")
    public Result<SysMenu> getById(@RequestParam Long id) {
        return Result.ok(sysMenuService.getById(id));
    }
}

