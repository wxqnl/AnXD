package com.cug.daijiaguanli.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("system")
public interface SysRoleMapper {

    List<SysRole> selectWithCondition(@Param("roleName") String roleName,
                                      @Param("roleCode") String roleCode,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    long countWithCondition(@Param("roleName") String roleName,
                            @Param("roleCode") String roleCode);

    void insert(SysRole role);
    void update(SysRole role);
    void delete(Long id);
    SysRole selectById(Long id);
}
