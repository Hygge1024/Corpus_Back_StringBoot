package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.controller.utils.Result;
import com.lt.domain.Task;
import com.lt.domain.students;

import java.util.List;

public interface StudentsService extends IService<students> {
    //查询所有的学生信息——用不到
    List<students> getStuAll();

    //查询单独的学生 sid
    students getBySid(Integer sid);

    //根据学号查询学生信息——学号
    students getByStunumber(String stunumber);

    //查询学生信息分页查询——常用
    IPage<students> getPage(int currentPage, int pageSize);

    //查询学生信息分页查询——根据班级
    IPage<students> getByClassPage(String cname, int currentPage, int pageSize);

    //注册
    Result registe(students stu);

    //登录检测
    students loginCheck(students stu);

    //删除
    int deleteByNumber(String stunumber);

    //更新
    int update(students stu);

    //更新班级信息
    int updateClass(String oldClassName, String newClassName);

    //获取发布的练习信息
    List<Task> getTaskBySelf(String stunumber);

}
