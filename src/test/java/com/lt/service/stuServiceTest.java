package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Result;
import com.lt.domain.students;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class stuServiceTest {
    @Autowired
    private StudentsService studentsService;

    @Test
    void getStuAll() {
        System.out.println(studentsService.getStuAll());
    }

    @Test
    void getBySid() {
        System.out.println(studentsService.getBySid(1));
    }

    @Test
    void getByStunumber() {
        String num = "20230001";
        System.out.println(studentsService.getByStunumber(num));
    }

    @Test
    void getPage() {
        int cur = 1;
        int size = 2;
        IPage<students> page = studentsService.getPage(cur, size);
        System.out.println(page.getCurrent());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.getPages());
        System.out.println(page.getRecords());
    }

    @Test
    void getByClassPage() {
        System.out.println(studentsService.getByClassPage("软件2102", 1, 2).getRecords());
    }

    @Test
    void registeTest() {
        students newStu = new students();
        newStu.setStuname("测试对象");
        newStu.setStunumber("200211111");
        newStu.setStuphone("12345678912");
        newStu.setStupassword("123456");
        newStu.setStusex(1);
        newStu.setStuclass("软件2102");
        Result rUser = studentsService.registe(newStu);
        System.out.println(rUser);
    }

    @Test
    void loginCheck() {
        students stu = new students();
        stu.setStunumber("2002110");
        stu.setStupassword("123456");
        System.out.println(studentsService.loginCheck(stu));
    }

    @Test
    void delete() {
        System.out.println(studentsService.deleteByNumber("200211111"));
    }

    @Test
    void update() {
        students newStu = new students();
        newStu.setStuname("测试对象+更新");
        newStu.setStunumber("200211");
        newStu.setStuphone("11111111111");
        newStu.setStupassword("123456");
        newStu.setStusex(1);
        newStu.setStuclass("软件2102");
        System.out.println(studentsService.update(newStu));
    }

    @Test
    void getTaskBySelf() {
        System.out.println(studentsService.getTaskBySelf("210003"));
    }

}
