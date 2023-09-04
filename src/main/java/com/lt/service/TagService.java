package com.lt.service;

import com.lt.doadmin.Tag;

import java.util.List;

public interface TagService {
    //查询
    List<Tag> findAll();
    //添加
    int create(Tag tag);
    //删除
    int delete(int tid);
    //更新
    int update(Tag tag);
}
