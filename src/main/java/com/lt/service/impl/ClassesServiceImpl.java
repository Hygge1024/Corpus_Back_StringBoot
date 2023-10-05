package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.classDao;
import com.lt.doadmin.Classes;
import com.lt.service.ClassesService;
import com.lt.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassesServiceImpl extends ServiceImpl<classDao, Classes> implements ClassesService {
    @Autowired
    private classDao classDao;
    @Autowired
    private StudentsService studentsService;

    @Override
    public List<Classes> getAllClass() {
        return classDao.selectList(null);
    }

    @Override
    public List<Classes> getBySelf(String tnumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", tnumber);
        return classDao.selectList(wrapper);
    }

    @Override
    public int addClss(Classes classes) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cname", classes.getCname());

        Classes oldClass = classDao.selectOne(wrapper);
        if (null != oldClass) {
            return 0;//表示已经存在了，不需要再添加班级了
        }
        return classDao.insert(classes);
    }

    @Override
    public int deleteByCid(int cid) {
        //在删除前需要对改班级的学生班级信息进行清零
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid", cid);
        Classes classes = classDao.selectOne(wrapper);
        String oldClassName = classes.getCname();
        String newClassName = "delete";
//        调用学生更新班级的接口
        studentsService.updateClass(oldClassName, newClassName);
        return classDao.delete(wrapper);
    }

    @Override
    public int update(Classes classes) {

//        新名称
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cname", classes.getCname());
        Classes C = classDao.selectOne(wrapper);
        if (null != C) {
            return 0;//表示新名字重复了
        }
        wrapper.clear();
        //需要先根据cid查询班级名oldClassName，再调用学生更新班级名称的接口来
        wrapper.eq("cid", classes.getCid());
        String oldClassName = classDao.selectOne(wrapper).getCname();
        System.out.println("cid的旧名字为：" + oldClassName);
        System.out.println("cid的新名字为：" + classes.getClass());
        //调用接口——更改所有对应学生的班级信息
        int end = studentsService.updateClass(oldClassName, classes.getCname());
//        if (end <= 0) {
//            return 0;
//        }
        wrapper.clear();
        wrapper.eq("cid", classes.getCid());
        C = classDao.selectOne(wrapper);
        C.setCname(classes.getCname());
        return classDao.update(C, wrapper);
    }
}
