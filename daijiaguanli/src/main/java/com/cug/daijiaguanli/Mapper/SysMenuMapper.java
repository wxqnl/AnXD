package com.cug.daijiaguanli.mapper;

import com.cug.daijiaguanli.enetity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAll();
    void insert(SysMenu menu);
    void update(SysMenu menu);
    void delete(Long id);
    SysMenu selectById(Long id);
}
