package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.doadmin.Exercises;
import com.lt.doadmin.ExercisesDao;
import com.lt.service.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    @Autowired
    private ExercisesService exercisesService;

    @GetMapping("/{stuId}")
    public Result getAllExercises(@PathVariable String stuId) {
        List<Exercises> exercisesList = exercisesService.getAllExercises(stuId);
        Integer code = exercisesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercisesList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, exercisesList);
    }

    @GetMapping("/byEid/{eid}")
    public Result getOneExercise(@PathVariable int eid) {
        Exercises exercises = exercisesService.getOneExercises(eid);
        Integer code = exercises != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercises != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, exercises);
    }

    @GetMapping("{currentPage}/{pageSize}/{stuId}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String stuId) {
        List<Exercises> exercisesList = exercisesService.getPage(currentPage, pageSize, stuId);
        Integer code = exercisesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercisesList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, exercisesList);
    }

    @PostMapping(value = "/upload")
    public Result uploadStuFile(@RequestParam("multipartFile") MultipartFile multipartFile, @ModelAttribute ExercisesDao exercisesDao) {
        Long StuID = exercisesService.upload(multipartFile);
        System.out.println("上传后的文件地址为: " + StuID);
        exercisesDao.setStuFile(StuID);
        int flag = exercisesService.create(exercisesDao);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "上传成功" : "上传失败，出现重复属性";
        return new Result(code, msg, exercisesDao);
    }

    @PutMapping
    public Result update(@ModelAttribute ExercisesDao exercisesDao) {
        int flag = exercisesService.update(exercisesDao);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "更新成功" : "更新失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/{eid}")
    public Result delete(@PathVariable int eid) {
        int flag = exercisesService.delete(eid);
        Integer code = flag != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag != 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, null);
    }
}
