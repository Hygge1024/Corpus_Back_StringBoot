package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.doadmin.Classes;

import java.util.List;

public interface ClassesService extends IService<Classes> {
    //添加班级
    int addClss(Classes classes);

    //查询所有班级——bug分页查询，如果很多
//    List<Classes> getAllClass();
    IPage<Classes> getAllClass(int currentPage, int pageSize);

    //查询——cid
    Classes getOne(int cid);

    //删除班级，根据班级名
    int deleteByName(String cname);

    //更新班级名（班级名是唯一的）
    int update(String oldCname, String newCname);
}
