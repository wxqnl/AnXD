package com.cug.daijiaguanli.controller;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysDept;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.SysDeptService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value="/sysDept")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysDeptController {


    @Autowired
    private SysDeptService sysDeptService;

    // 获取全部部门树
//    @GetMapping("/list")
//    public Result<List<SysDept>> list() {
//        return Result.ok(sysDeptService.getDeptTree());
//    }

    @GetMapping("/list")
    public Result<PageBean<SysDept>> list(Integer pageSize, Integer pageNum,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) Integer parentId,
                                          @RequestParam(required = false) String children,
                                          @RequestParam(required = false) String leader,
                                          @RequestParam(required = false) Integer status) {
        PageBean<SysDept>pd=sysDeptService.getAllDept(pageSize,pageNum,name,parentId,children,leader,status);
        return Result.ok(pd);
    }

    // 添加部门
    @PostMapping("/add")
    public Result add(@RequestBody SysDept dept) {
        sysDeptService.addDept(dept);
        return Result.ok();
    }

    // 更新部门
    @PostMapping("/update")
    public Result update(@RequestBody SysDept dept) {
        sysDeptService.updateDept(dept);
        return Result.ok();
    }

    // 删除部门
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id) {
        sysDeptService.deleteDept(id);
        return Result.ok();
    }

    // 查询单个部门
    @GetMapping("/getById")
    public Result<SysDept> getById(@RequestParam Long id) {
        return Result.ok(sysDeptService.getById(id));
    }

}

