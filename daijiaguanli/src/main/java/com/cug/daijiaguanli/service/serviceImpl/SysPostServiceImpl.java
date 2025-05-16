package com.cug.daijiaguanli.service.Impl;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysPost;
import com.cug.daijiaguanli.mapper.SysPostMapper;
import com.cug.daijiaguanli.service.SysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysPostServiceImpl implements SysPostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public PageBean<SysPost> getPostList(Integer pageNum, Integer pageSize, String name, String postCode, Integer status) {
        int offset = (pageNum - 1) * pageSize;
        List<SysPost> list = sysPostMapper.selectWithCondition(name, postCode, status, offset, pageSize);
        long count = sysPostMapper.countWithCondition(name, postCode, status);
        return new PageBean<>(count, list);
    }

    @Override
    public void addPost(SysPost post) {
        sysPostMapper.insert(post);
    }

    @Override
    public void updatePost(SysPost post) {
        sysPostMapper.update(post);
    }

    @Override
    public void deletePost(Long id) {
        sysPostMapper.delete(id);
    }

    @Override
    public SysPost getById(Long id) {
        return sysPostMapper.selectById(id);
    }

}
