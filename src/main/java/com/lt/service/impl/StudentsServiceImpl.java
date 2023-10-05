package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.dao.studentDao;
import com.lt.doadmin.CorpusDao;
import com.lt.doadmin.students;
import com.lt.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsServiceImpl extends ServiceImpl<studentDao, students> implements StudentsService {
    @Autowired
    private studentDao studentDao;

    @Override
    public List<students> getStuAll() {
        return studentDao.selectList(null);
    }

    @Override
    public students getBySid(Integer sid) {
        return studentDao.selectById(sid);
    }

    @Override
    public students getByStunumber(String stunumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", stunumber);
        return studentDao.selectOne(wrapper);
    }

    @Override
    public IPage<students> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        studentDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<students> getByClassPage(String cname, int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stuclass", cname);

        return studentDao.selectPage(page, wrapper);
    }

    @Override
    public Result registe(students stu) {
//        String usernumber = stu.getStunumber();
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("stunumber", usernumber);
//        students user = studentDao.selectOne(wrapper);//先判断是否存在
//        if(user != null){
//            return user;//表示已经存在了，返回空对象
//        }else{
//            String hashedPassword = hashPassword(stu.getStupassword());//对密码进行加密算法
//            stu.setStupassword(hashedPassword);
//            studentDao.insert(stu);
//            return stu;//登录成功时，返回不为空的注册对象信息
//        }
        String usernumber = stu.getStunumber();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", usernumber);
        students user = studentDao.selectOne(wrapper);
        if (user != null) {
            return new Result(Code.SAVE_ERR, "用户已存在，请重新输入!", stu);//失败时，返回的是用户输入的信息
        } else {
            String hashedPassword = hashPassword(stu.getStupassword());
            stu.setStupassword(hashedPassword);
            int flag = studentDao.insert(stu);
            return new Result(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, "恭喜你,注册成功!", stu);//flag表示影响的行数msg
        }
    }

    @Override
    public students loginCheck(students stu) {
        String usernumber = stu.getStunumber();
        String password = stu.getStupassword();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", usernumber);
        students user = studentDao.selectOne(wrapper);
        return user;
    }

    @Override
    public int deleteByNumber(String stunumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", stunumber);
        return studentDao.delete(wrapper);
    }

    @Override
    public int update(students stu) {
        String hashedPassword = hashPassword(stu.getStupassword());//对新密码进行加密
        stu.setStupassword(hashedPassword);
        QueryWrapper wrapper = new QueryWrapper();
        String stunumber = stu.getStunumber();
        wrapper.eq("stunumber", stu.getStunumber().toString());
        int flag = studentDao.update(stu, wrapper);
        return flag;
    }

    @Override
    public int updateClass(String oldClassName, String newClassName) {
        System.out.println(oldClassName + "   " + newClassName);
        if ("delete".equals(newClassName)) {
            newClassName = " ";
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stuclass", oldClassName);
        List<students> studentsList = studentDao.selectList(wrapper);
//        System.out.println("成功查询所有的班级信息");
        wrapper.clear();
        for (students stu : studentsList) {
//            System.out.println(stu);
            stu.setStuclass(newClassName);
            wrapper.eq("stunumber", stu.getStunumber().toString());
            studentDao.update(stu, wrapper);
            wrapper.clear();
        }
        return studentsList.size();
    }

    /*
   哈希加密方法
    */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

}
