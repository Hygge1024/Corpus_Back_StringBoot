package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.classDao;
import com.lt.doadmin.Classes;
import com.lt.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassesServiceImpl extends ServiceImpl<classDao, Classes> implements ClassesService {
    @Autowired
    private classDao classDao;

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
    public IPage<Classes> getAllClass(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        return classDao.selectPage(page, null);
    }

    @Override
    public Classes getOne(int cid) {
        return classDao.selectById(cid);
    }

    @Override
    public int deleteByName(String cname) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cname", cname);
        return classDao.delete(wrapper);
    }

    @Override
    public int update(String oldCname, String newCname) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cname", newCname);
        Classes C = classDao.selectOne(wrapper);
        if (null != C) {
            return 0;//表示新名字重复了
        }
        wrapper.clear();
        wrapper.eq("cname", oldCname);
        C = classDao.selectOne(wrapper);
        C.setCname(newCname);
        return classDao.update(C, wrapper);
    }
}
