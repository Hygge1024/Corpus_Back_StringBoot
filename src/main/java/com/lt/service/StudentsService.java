package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.controller.utils.Result;
import com.lt.doadmin.CorpusDao;
import com.lt.doadmin.students;

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

    //注册
    Result registe(students stu);

    //登录检测
    students loginCheck(students stu);

    //删除
    int deleteById(Integer sid);

    //更新
    int update(students stu);

}
