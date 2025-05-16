package com.cug.daijiaguanli.service.Impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.customer.CustomerInfo;
import com.cug.daijiaguanli.enetity.system.SysDept;
import com.cug.daijiaguanli.mapper.SysDeptMapper;
import com.cug.daijiaguanli.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDept> getDeptTree() {
        List<SysDept> all = sysDeptMapper.selectAll();
        return buildTree(all, 0L);
    }

    private List<SysDept> buildTree(List<SysDept> all, Long parentId) {
        List<SysDept> children = new ArrayList<>();
        for (SysDept dept : all) {
            if (parentId.equals(dept.getParentId())) {
                dept.setChildren(buildTree(all, dept.getId()));
                children.add(dept);
            }
        }
        return children;
    }

    @Override
    public void addDept(SysDept dept) {
        sysDeptMapper.insert(dept);
    }

    @Override
    public void updateDept(SysDept dept) {
        sysDeptMapper.update(dept);
    }

    @Override
    public void deleteDept(Long id) {
        sysDeptMapper.delete(id);
    }

    @Override
    public SysDept getById(Long id) {
        return sysDeptMapper.selectById(id);
    }

    @Override
    public PageBean<SysDept> getAllDept(Integer pageSize, Integer pageNum, String name, Integer parentId, String children, String leader, Integer status) {
        int start = (pageNum - 1) * pageSize;
        List<SysDept> items = sysDeptMapper.selectAllWithCondition(name, parentId, children, leader,status, start, pageSize);
        Long total = sysDeptMapper.countAllWithCondition(name, parentId, children, leader,status);
        return new PageBean<>(total, items);
    }

}
