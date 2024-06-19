package com.lt.service;

import com.lt.domain.two_level;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LevelServiceTest {
    @Autowired
    private level_twoService levelTwoService;
    @Test
    void test1(){
        two_level t1 = new two_level();
        t1.setExercisesId(2);
        t1.setType(3);
        t1.setTwoComment("很好21");
        t1.setTwoScore(121.20);
        t1.setTwoName("语法准确度21");
        levelTwoService.insert(t1);
    }
    @Test
    void test2(){
        System.out.println(levelTwoService.getall(2,3));
    }
    @Test
    void test3(){
        two_level newt = new two_level();
        newt.setTwoId(2);
        newt.setExercisesId(1);
        newt.setTwoComment("是的是的");
        newt.setTwoScore(12233);
        levelTwoService.update(newt);
    }
}
