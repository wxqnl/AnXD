package com.cug.daijiaguanli.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.system.SysPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("system")
public interface SysPostMapper {

    List<SysPost> selectWithCondition(@Param("name") String name,
                                      @Param("postCode") String postCode,
                                      @Param("status") Integer status,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    long countWithCondition(@Param("name") String name,
                            @Param("postCode") String postCode,
                            @Param("status") Integer status);

    void insert(SysPost post);
    void update(SysPost post);
    void delete(Long id);
    SysPost selectById(Long id);
}
