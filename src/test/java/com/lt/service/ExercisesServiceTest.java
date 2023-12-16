package com.lt.service;

import com.lt.domain.ExercisesDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

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
    void getByCid() {
        System.out.println(exercisesService.getByCid(7));
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
//        exercisesDao.setScore(99);
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
//        exercisesDao.setScore(911);
        exercisesDao.setIdentifyText("我是新建部分");
        exercisesService.create(exercisesDao);
        System.out.println("完成");
    }

    @Test
    void delete() {
        System.out.println(exercisesService.delete(2));
    }

    @Test
    void getVolume() {
        exercisesService.getautoScore(1);
    }

    @Test
    void deleteFile() {
        String outputFilePath = "src/main/resources/static/temp.wav";//零时的文件——运行结束就删除了
        File outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            try {
                if (outputFile.delete()) {
                    System.out.println("输出文件已删除");
                } else {
                    System.err.println("无法删除输出文件");
                }
            } catch (SecurityException e) {
                System.err.println("没有足够的权限来删除文件: " + e.getMessage());
            }
        }
    }

    @Test
    void getExerciseByClass() {
        System.out.println(exercisesService.getExerciseByClass("英语2101"));
    }
}
