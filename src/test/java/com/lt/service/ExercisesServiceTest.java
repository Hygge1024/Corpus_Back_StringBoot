package com.lt.service;

import com.lt.doadmin.ExercisesDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExercisesServiceTest {
    @Autowired
    private ExercisesService exercisesService;

    @Test
    void getAllExercises() {
        System.out.println(exercisesService.getAllExercises("123"));
    }

    @Test
    void getOneExercises() {
        System.out.println(exercisesService.getOneExercises(1));
    }

    @Test
    void getPage() {
        System.out.println(exercisesService.getPage(1, 1, "123"));
    }

    @Test
    void update() {
        ExercisesDao exercisesDao = new ExercisesDao();
        exercisesDao.setId(1);
        exercisesDao.setStuID("123");
        exercisesDao.setStuFile(2L);
        exercisesDao.setCorpus(7);
        exercisesDao.setScore(99);
        exercisesDao.setIdentifyText("我现在是test函数部分");
        exercisesService.update(exercisesDao);
        System.out.println("完成");
    }

    @Test
    void create() {
        ExercisesDao exercisesDao = new ExercisesDao();
        exercisesDao.setStuID("122222");
        exercisesDao.setStuFile(3L);
        exercisesDao.setCorpus(25);
        exercisesDao.setScore(911);
        exercisesDao.setIdentifyText("我是新建部分");
        exercisesService.create(exercisesDao);
        System.out.println("完成");
    }

    @Test
    void delete() {
        System.out.println(exercisesService.delete(2));
    }
}
