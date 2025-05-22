package com.cug.daijiaguanli.service.Impl;


import com.cug.daijiaguanli.enetity.system.SysMenu;
import com.cug.daijiaguanli.mapper.SysMenuMapper;
import com.cug.daijiaguanli.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> allMenus = sysMenuMapper.selectAll();
        return buildTree(allMenus, 0L);
    }

    private List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu node : all) {
            if (parentId.equals(node.getParentId())) {
                node.setChildren(buildTree(all, node.getId()));
                tree.add(node);
            }
        }
        return tree;
    }

    @Override
    public void addMenu(SysMenu menu) {
        sysMenuMapper.insert(menu);
    }

    @Override
    public void updateMenu(SysMenu menu) {
        sysMenuMapper.update(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        sysMenuMapper.delete(id);
    }

    @Override
    public SysMenu getById(Long id) {
        return sysMenuMapper.selectById(id);
    }
}
