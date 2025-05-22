package com.cug.daijiaguanli.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.system.SysDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DS("system")
public interface SysDeptMapper {
    List<SysDept> selectAll();
    void insert(SysDept dept);
    void update(SysDept dept);
    void delete(Long id);
    SysDept selectById(Long id);

    List<SysDept> selectAllWithCondition(String name, Integer parentId, String children, String leader, Integer status, int start, Integer pageSize);

    Long countAllWithCondition(String name, Integer parentId, String children, String leader, Integer status);
}
