package com.lt.service;

import com.lt.domain.three_level;
import com.lt.domain.two_level;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LevelServiceTest {
    @Autowired
    private level_twoService levelTwoService;
    @Autowired
    private level_threeService levelThreeService;
    @Test
    void test1(){
        two_level t1 = new two_level();
        t1.setExercisesId(88);
        t1.setType(3);
        t1.setTwoComment("很好33");
        t1.setTwoScore(121.20);
        t1.setTwoName("语法准确度21");
        levelTwoService.insert(t1);
    }
    @Test
    void test2(){
        System.out.println(levelTwoService.getall(2));
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
    @Test
    void three1(){
        three_level l = new three_level();
        l.setThreeName("测试");
        l.setThreeComment("ces");
        l.setThreeScore(22.32);
        l.setTwoId(2);
        levelThreeService.insert(l);
    }
    @Test
    void three_get(){
        System.out.println(levelThreeService.getall(2));
    }
    @Test
    void threeUpdate(){
        three_level l = new three_level();
        l.setThreeId(1);
        l.setThreeName("测lih试22");
        l.setThreeComment("ces2");
        l.setThreeScore(22.32);
        l.setTwoId(2);
        System.out.println(levelThreeService.update(l));
    }
}
