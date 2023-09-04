package com.lt.service;

import com.lt.doadmin.Classes;
import com.lt.service.impl.ClassesServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ClassesServiceTest {
    @Autowired
    private ClassesService classesService;

    @Test
    void insert() {
        Classes classes = new Classes();
//        classes.setCid(4);
        classes.setCname("软件测试班级2102");
        System.out.println(classesService.addClss(classes));
    }

    @Test
    void getClassAll() {
        List<Classes> classesList = classesService.getAllClass(1, 3).getRecords();
        System.out.println(classesList);
    }

    @Test
    void getOne() {
        System.out.println(classesService.getOne(2));
    }

    @Test
    void deleteByName() {
        System.out.println(classesService.deleteByName("软件测试班级21002"));
    }

    @Test
    void update() {
        String oldName = "软件2101";
        String newName = "软件2101-1";
        System.out.println(classesService.update(oldName, newName));
    }
}
