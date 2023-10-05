package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.dao.teacherDao;
import com.lt.doadmin.teachers;
import com.lt.service.ClassesService;
import com.lt.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachersServiceImpl extends ServiceImpl<teacherDao, teachers> implements TeachersService {
    @Autowired
    private teacherDao teacherDao;
    @Autowired
    private ClassesService classesService;

    @Override
    public List<teachers> getTeaAll() {
        return teacherDao.selectList(null);
    }

    @Override
    public teachers getByTid(Integer tid) {
        return teacherDao.selectById(tid);
    }

    @Override
    public teachers getByTeanumber(String teanumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", teanumber);
        return teacherDao.selectOne(wrapper);
    }

    @Override
    public IPage<teachers> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        teacherDao.selectPage(page, null);
        return page;
    }

    @Override
    public Result registe(teachers tea) {
        String usernumber = tea.getTnumber();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", usernumber);
        teachers user = teacherDao.selectOne(wrapper);
        if (null != user) {
            return new Result(Code.SAVE_ERR, "用户已存在，请重新输入!", tea);
        } else {
            String hashedPassword = hashPassword(tea.getTeapassword());
            tea.setTeapassword(hashedPassword);
            int flag = teacherDao.insert(tea);
            return new Result(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, "恭喜你,注册成功!", tea);
        }
    }

    @Override
    public teachers loginCheck(teachers tea) {
        String usernumber = tea.getTnumber();
        String password = tea.getTeapassword();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", usernumber);
        teachers user = teacherDao.selectOne(wrapper);
        return user;
    }

    @Override
    public int deleteById(Integer tid) {
        return teacherDao.deleteById(tid);
    }

    @Override
    public int update(teachers tea) {
        String hashedPassword = hashPassword(tea.getTeapassword());//对密码加密
        tea.setTeapassword(hashedPassword);
        QueryWrapper wrapper = new QueryWrapper();
        String teanumber = tea.getTnumber();
        wrapper.eq("tnumber", tea.getTnumber().toString());
        int flag = teacherDao.update(tea, wrapper);
        return flag;
    }

    /*
  哈希加密方法
   */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
