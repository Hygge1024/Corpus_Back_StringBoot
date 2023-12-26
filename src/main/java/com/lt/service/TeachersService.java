package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.controller.utils.Result;
import com.lt.domain.Charts;
import com.lt.domain.Task;
import com.lt.domain.teachers;

import java.util.List;

public interface TeachersService extends IService<teachers> {
    //查询所有——不用的
    List<teachers> getTeaAll();

    //根据tid查询单个教师用户信息
    teachers getByTid(Integer tid);

    //根据工号查询
    teachers getByTeanumber(String teanumber);

    //查询所有——分页查询
    IPage<teachers> getPage(int currentPage, int pageSize);

    //注册教师用户
    Result registe(teachers tea);

    //登录检测
    teachers loginCheck(teachers tea);

    //删除
    int deleteById(Integer tid);

    //更新
    int update(teachers tea);

    //教师发布自己的语料
    int publish(Task task);

    int notpublish(int cid, String teanumber);

    List<Charts> getCharts(String className);

}
