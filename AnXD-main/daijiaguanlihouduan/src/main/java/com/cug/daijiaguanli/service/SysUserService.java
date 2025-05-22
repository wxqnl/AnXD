package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysLoginLog;
import com.cug.daijiaguanli.enetity.system.SysOperLog;
import com.cug.daijiaguanli.enetity.system.SysUser;
import org.springframework.stereotype.Service;

import java.util.Date;


public interface SysUserService {

    SysUser findByUserName(String username);

    PageBean<SysUser> SearchAllUser(Integer pageNum, Integer pageSize,
                                    String id, Byte username, String name, String phone,
                                    String deptId, String postId, String status, String postName, String deptName);

    SysUser getById(Long id);

    boolean save(SysUser sysUser);

    boolean updateById(SysUser sysUser);

    boolean removeById(Long id);



    PageBean<SysLoginLog> getUserLoginLog(Integer pageNum, Integer pageSize, String username, Date minAccessTime, Date maxAccessTime);

    PageBean<SysOperLog> getUserOperLog(Integer pageNum, Integer pageSize,
                                        String operName, String deptName, Integer status,
                                        String operatorType, String businessType, String requestMethod,
                                        Date minOperTime, Date maxOperTime);
}
