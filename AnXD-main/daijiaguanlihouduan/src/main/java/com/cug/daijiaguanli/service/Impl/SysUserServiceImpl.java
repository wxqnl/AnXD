package com.cug.daijiaguanli.service.Impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysLoginLog;
import com.cug.daijiaguanli.enetity.system.SysOperLog;
import com.cug.daijiaguanli.enetity.system.SysUser;
import com.cug.daijiaguanli.mapper.SysUserMapper;
import com.cug.daijiaguanli.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override

    public SysUser findByUserName(String username)
    {
        return sysUserMapper.findByUserName(username);
    }

    @Override
    public PageBean<SysUser> SearchAllUser(Integer pageNum, Integer pageSize,
                                           String id, Byte username, String name, String phone,
                                           String deptId, String postId, String status, String postName, String deptName) {
        int offset = (pageNum - 1) * pageSize;
        List<SysUser> users = sysUserMapper.selectAllWithCondition(id, username, name, phone, deptId, postId, status, postName, deptName, offset, pageSize);
        long count = sysUserMapper.countAllWithCondition(id, username, name, phone, deptId, postId, status, postName, deptName);
        return new PageBean<>(count, users);
    }

    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public boolean save(SysUser sysUser) {
        return sysUserMapper.insert(sysUser) > 0;
    }

    @Override
    public boolean updateById(SysUser sysUser) {
        return sysUserMapper.update(sysUser) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return sysUserMapper.delete(id) > 0;
    }

    @Override
    public PageBean<SysLoginLog> getUserLoginLog(Integer pageNum, Integer pageSize, String username, Date minAccessTime, Date maxAccessTime) {
        int offset = (pageNum - 1) * pageSize;
        List<SysLoginLog> list = sysUserMapper.selectLoginLogsByCondition(username, minAccessTime, maxAccessTime, offset, pageSize);
        long count = sysUserMapper.countLoginLogsByCondition(username, minAccessTime, maxAccessTime);
        return new PageBean<>(count, list);
    }

    @Override
    public PageBean<SysOperLog> getUserOperLog(Integer pageNum, Integer pageSize, String operName, String deptName, Integer status, String operatorType, String businessType, String requestMethod, Date minOperTime, Date maxOperTime) {
        int offset = (pageNum - 1) * pageSize;
        List<SysOperLog> list = sysUserMapper.selectOperLogsByCondition(operName, deptName, status, operatorType, businessType, requestMethod, minOperTime, maxOperTime, offset, pageSize);
        long count = sysUserMapper.countOperLogsByCondition(operName, deptName, status, operatorType, businessType, requestMethod, minOperTime, maxOperTime);
        return new PageBean<>(count, list);
    }


}
