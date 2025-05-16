package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.system.SysMenu;

import java.util.List;

public interface SysMenuService {

    List<SysMenu> getMenuTree();
    void addMenu(SysMenu menu);
    void updateMenu(SysMenu menu);
    void deleteMenu(Long id);
    SysMenu getById(Long id);
}
