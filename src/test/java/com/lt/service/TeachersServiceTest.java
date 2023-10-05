package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Result;
import com.lt.doadmin.teachers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TeachersServiceTest {
    @Autowired
    private TeachersService teachersService;

    @Test
    void getAll() {
        System.out.println(teachersService.getTeaAll());
    }

    @Test
    void getBySid() {
        System.out.println(teachersService.getByTid(1));
    }

    @Test
    void getByTeanumber() {
        String number = "100010";
        System.out.println(teachersService.getByTeanumber(number));
    }

    @Test
    void getPage() {
        int cur = 1;
        int size = 2;
        IPage<teachers> page = teachersService.getPage(cur, size);
        System.out.println(page.getCurrent());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.getPages());
        System.out.println(page.getRecords());
    }

    @Test
    void registeTest() {
        teachers newTea = new teachers();
        newTea.setTnumber("2023910");
        newTea.setTeaname("王老师");
        newTea.setTeapassword("123456");
//        newTea.setTeachaclass("软件2101");
        newTea.setTsex(0);
        Result rUser = teachersService.registe(newTea);
        System.out.println(rUser.getMsg());
    }

    @Test
    void loginCheck() {
        teachers tea = new teachers();
        tea.setTnumber("100010");
        tea.setTeapassword("123456");
        System.out.println(teachersService.loginCheck(tea));
    }

    @Test
    void delete() {
        System.out.println(teachersService.deleteById(2));
    }

    @Test
    void update() {
        teachers newTea = new teachers();
        newTea.setTnumber("100010");
        newTea.setTeaname("gengxin王老师");
        newTea.setTeapassword("123456");
        newTea.setTsex(0);
        System.out.println(teachersService.update(newTea));
    }

//    @Test
//    void getAllSelfClass() {
//        List<teaclass> teaclassList = teachersService.getAllSelfClass("123001");
//        System.out.println(teaclassList);
//    }
//
//    @Test
//    void insertClass() {
//        teaclass teaclass = new teaclass();
//        teaclass.setCid(100);
//        teaclass.setTnumber("100101666");
//        int flag = teachersService.insertSelfClass(teaclass);
//        System.out.println(flag);
//    }
//
//    @Test
//    void deleteSelf() {
//        teaclass teaclass = new teaclass();
//        teaclass.setCid(3);
//        teaclass.setTnumber("123001");
//        System.out.println(teachersService.deleteSelf(teaclass));
//    }
}
