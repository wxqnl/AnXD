package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.system.SysPost;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface SysPostService {
    PageBean<SysPost> getPostList(Integer pageNum, Integer pageSize, String name, String postCode, Integer status);
    void addPost(SysPost post);
    void updatePost(SysPost post);
    void deletePost(Long id);
    SysPost getById(Long id);
}
