package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysDept;

import java.util.List;

public interface SysDeptService {
    List<SysDept> getDeptTree();
    void addDept(SysDept dept);
    void updateDept(SysDept dept);
    void deleteDept(Long id);
    SysDept getById(Long id);

    PageBean<SysDept> getAllDept(Integer pageSize, Integer pageNum, String name, Integer parentId, String children, String leader, Integer status);
}
