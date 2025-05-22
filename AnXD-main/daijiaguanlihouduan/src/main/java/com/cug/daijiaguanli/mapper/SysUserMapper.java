package com.cug.daijiaguanli.mapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.system.SysLoginLog;
import com.cug.daijiaguanli.enetity.system.SysOperLog;
import com.cug.daijiaguanli.enetity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@DS("system")
public interface SysUserMapper {
    SysUser findByUserName(String username);

    List<SysUser> selectAllWithCondition(@Param("id") String id,
                                         @Param("username") Byte username,
                                         @Param("name") String name,
                                         @Param("phone") String phone,
                                         @Param("deptId") String deptId,
                                         @Param("postId") String postId,
                                         @Param("status") String status,
                                         @Param("postName") String postName,
                                         @Param("deptName") String deptName,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    long countAllWithCondition(@Param("id") String id,
                               @Param("username") Byte username,
                               @Param("name") String name,
                               @Param("phone") String phone,
                               @Param("deptId") String deptId,
                               @Param("postId") String postId,
                               @Param("status") String status,
                               @Param("postName") String postName,
                               @Param("deptName") String deptName);

    SysUser selectById(Long id);

    int insert(SysUser sysUser);

    int update(SysUser sysUser);

    int delete(Long id);

    List<SysLoginLog> selectLoginLogsByCondition(@Param("username") String username,
                                            @Param("minAccessTime") Date minAccessTime,
                                            @Param("maxAccessTime") Date maxAccessTime,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    long countLoginLogsByCondition(@Param("username") String username,
                              @Param("minAccessTime") Date minAccessTime,
                              @Param("maxAccessTime") Date maxAccessTime);

    List<SysOperLog> selectOperLogsByCondition(@Param("operName") String operName,
                                           @Param("deptName") String deptName,
                                           @Param("status") Integer status,
                                           @Param("operatorType") String operatorType,
                                           @Param("businessType") String businessType,
                                           @Param("requestMethod") String requestMethod,
                                           @Param("minOperTime") Date minOperTime,
                                           @Param("maxOperTime") Date maxOperTime,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    long countOperLogsByCondition(@Param("operName") String operName,
                              @Param("deptName") String deptName,
                              @Param("status") Integer status,
                              @Param("operatorType") String operatorType,
                              @Param("businessType") String businessType,
                              @Param("requestMethod") String requestMethod,
                              @Param("minOperTime") Date minOperTime,
                              @Param("maxOperTime") Date maxOperTime);

}
