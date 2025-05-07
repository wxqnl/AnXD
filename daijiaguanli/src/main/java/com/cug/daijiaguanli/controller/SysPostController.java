package com.cug.daijiaguanli.controller;


import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysPost;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.SysPostService;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@Tag(name = "岗位管理")
@RestController
@RequestMapping(value="/sysPost")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysPostController {
    @Autowired
    private SysPostService sysPostService;

    // 分页查询岗位信息
    @GetMapping("/list")
    public Result<PageBean<SysPost>> list(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String postCode,
                                          @RequestParam(required = false) Integer status) {
        PageBean<SysPost> page = sysPostService.getPostList(pageNum, pageSize, name, postCode, status);
        return Result.ok(page);
    }

    // 添加岗位
    @PostMapping("/add")
    public Result add(@RequestBody SysPost post) {
        sysPostService.addPost(post);
        return Result.ok();
    }

    // 更新岗位
    @PostMapping("/update")
    public Result update(@RequestBody SysPost post) {
        sysPostService.updatePost(post);
        return Result.ok();
    }

    // 删除岗位
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id) {
        sysPostService.deletePost(id);
        return Result.ok();
    }

    // 获取单个岗位
    @GetMapping("/getById")
    public Result<SysPost> getById(@RequestParam Long id) {
        return Result.ok(sysPostService.getById(id));
    }
}

