package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysRole;

public interface SysRoleService {

    PageBean<SysRole> getRoleList(Integer pageNum, Integer pageSize, String roleName, String roleCode);
    void addRole(SysRole role);
    void updateRole(SysRole role);
    void deleteRole(Long id);
    SysRole getById(Long id);
}
