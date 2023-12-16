package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.dao.adminDao;
import com.lt.dao.teacherDao;
import com.lt.domain.admins;
import com.lt.domain.teachers;
import com.lt.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminsServiceImpl extends ServiceImpl<adminDao, admins> implements AdminsService {
    @Autowired
    private adminDao adminDao;
    @Autowired
    private teacherDao teacherDao;

    @Override
    public List<admins> getAdmAll() {
        return adminDao.selectList(null);
    }

    @Override
    public admins getByAid(Integer aid) {
        return adminDao.selectById(aid);
    }

    @Override
    public admins getByAdmnumber(String admnumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("anumber", admnumber);
        return adminDao.selectOne(wrapper);
    }

    @Override
    public Result registe(admins adm) {
        String usernumber = adm.getAnumber();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("anumber", usernumber);
        admins user = adminDao.selectOne(wrapper);
        if (null != user) {
            return new Result(Code.SAVE_ERR, "用户已存在，请重新输入!", adm);
        } else {
            String hashedPassword = hashPassword(adm.getAdmpassword());
            adm.setAdmpassword(hashedPassword);
            int flag = adminDao.insert(adm);
            return new Result(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, "恭喜你，注册成功", adm);
        }
    }

    @Override
    public admins loginCheck(admins adm) {
        String usernumber = adm.getAnumber();
        String password = adm.getAdmpassword();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("anumber", usernumber);
        admins user = adminDao.selectOne(wrapper);
        return user;
    }

    @Override
    public int deleteById(Integer tid) {
        return adminDao.deleteById(tid);
    }

    @Override
    public int update(admins adm) {
//        String hashedPassword = hashPassword(adm.getAdmpassword());
//        adm.setAdmpassword(hashedPassword);
        QueryWrapper wrapper = new QueryWrapper();
        String admnumber = adm.getAnumber();
        wrapper.eq("anumber", adm.getAnumber().toString());
        int flag = adminDao.update(adm, wrapper);
        return flag;
    }

    @Override
    public int OkTea(String teanumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", teanumber);
        teachers tea = teacherDao.selectOne(wrapper);
        System.out.println(teanumber);
        tea.setTeastate(1);
        int flag = teacherDao.update(tea, wrapper);
        return flag;
    }

    @Override
    public int NoTea(String teanumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", teanumber);
//        teachers tea = teacherDao.selectOne(wrapper);
//        tea.setTeastate(404);
//        int flag = teacherDao.update(tea, wrapper);
        int flag = teacherDao.delete(wrapper);
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
