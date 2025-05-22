package com.cug.daijiaguanli.service.Impl;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysRole;
import com.cug.daijiaguanli.mapper.SysRoleMapper;
import com.cug.daijiaguanli.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageBean<SysRole> getRoleList(Integer pageNum, Integer pageSize, String roleName, String roleCode) {
        int offset = (pageNum - 1) * pageSize;
        List<SysRole> list = sysRoleMapper.selectWithCondition(roleName, roleCode, offset, pageSize);
        long count = sysRoleMapper.countWithCondition(roleName, roleCode);
        return new PageBean<>(count, list);
    }

    @Override
    public void addRole(SysRole role) {
        sysRoleMapper.insert(role);
    }

    @Override
    public void updateRole(SysRole role) {
        sysRoleMapper.update(role);
    }

    @Override
    public void deleteRole(Long id) {
        sysRoleMapper.delete(id);
    }

    @Override
    public SysRole getById(Long id) {
        return sysRoleMapper.selectById(id);
    }
}
