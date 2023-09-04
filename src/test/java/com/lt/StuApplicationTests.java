package com.lt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lt.doadmin.students;
import com.lt.dao.studentDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//这里主要对students进行测试
@SpringBootTest
class StuApplicationTests {

    @Autowired
    private studentDao studentdao;

    //查询所有
    @Test
    void selectStudentAll() {
        System.out.println(studentdao.selectList(null));
    }
    //根据stunumber查找
    @Test
    void findByStunumber(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber","2135020633");
        System.out.println(studentdao.selectList(wrapper));
    }
    @Test
    void findById(){
        System.out.println(studentdao.selectById(2));
    }
    //添加操作
    @Test
    void AddStudents(){
        students stu1 = new students();
        stu1.setSid(3);
        stu1.setStunumber("1123123123");
        stu1.setStuclass("软件2102");
        stu1.setStuphone("12345678911");
        stu1.setStupassword("123456");
        stu1.setStusex(1);
        stu1.setStuname("小芳");
        studentdao.insert(stu1);
    }
    //删除操作
    @Test
    void deleteStu(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber","1123123123");
        studentdao.delete(wrapper);
    }
    //修改操作
    @Test
    void updateStu(){
        UpdateWrapper<students> wrapper = new UpdateWrapper<>();
        wrapper.eq("sid",2);
        wrapper.set("stuname","修改后的名称");
        studentdao.update(null,wrapper);
    }

}
