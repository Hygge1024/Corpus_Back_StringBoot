package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Exercises;
import com.lt.domain.TimeRange;
import com.lt.domain.teachers;
import com.lt.service.LabourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labour")
@CrossOrigin
@Slf4j
public class LabourController {
    @Autowired
    private LabourService labourService;
    private static String WenXinAPI;
    private static String WenXinSecurity;

    @Value("${baidu.WenXinAPI}")
    public void setWenXinAPI(String wenXinAPI) {
        LabourController.WenXinAPI = wenXinAPI;
    }

    @Value("${baidu.WenXinSecurity}")
    public void setWenXinSecurity(String WenXinSecurity) {
        LabourController.WenXinSecurity = WenXinSecurity;
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/createCorrection")
    public Result createCorrection(@RequestParam int correctId, @RequestBody List<teachers> teachers) {
        try {
            log.info("Received correctId: {}", correctId);
            log.info("Received teachers: {}", teachers);
            labourService.createCorrection(correctId, teachers);
            log.info("Correction created successfully");
            return new Result(Code.SAVE_OK, "批改表单创建成功", null);
        } catch (Exception e) {
            log.error("Error creating correction: ", e);
            return new Result(Code.SAVE_ERR, "批改表单创建失败", e.getMessage());
        }
    }

    @GetMapping("/queryExercises")
    public Result queryExercises(@RequestParam int correctId, @RequestParam int labourId) {
        List<Exercises> exercises = labourService.queryExercisesByCorrectIdAndLabourId(correctId, labourId);
        return new Result(Code.GET_OK, "查询成功", exercises);
    }

    @GetMapping("/queryUncorrectedCount")
    public Result queryUncorrectedCount(@RequestParam int correctId, @RequestParam int labourId) {
        int count = labourService.queryUncorrectedCount(correctId, labourId);
        return new Result(Code.GET_OK, "查询成功", count);
    }

    @GetMapping("/queryTimeRange")
    public Result queryTimeRange(@RequestParam int labourId) {
        TimeRange timeRange = labourService.queryTimeRange(labourId);
        return new Result(Code.GET_OK, "查询成功", timeRange);
    }

    @PostMapping("/autoCorrect")
    public Result autoCorrect(@RequestParam int correctId) {
        try {
            labourService.autoCorrect(correctId, WenXinAPI, WenXinSecurity);
            return new Result(Code.UPDATE_OK, "智能批改完成", null);
        } catch (Exception e) {
            return new Result(Code.UPDATE_ERR, "智能批改失败", e.getMessage());
        }
    }

    @GetMapping("/queryAverageScore")
    public Result queryAverageScore(@RequestParam int correctId) {
        double averageScore = labourService.queryAverageScore(correctId);
        return new Result(Code.GET_OK, "查询成功", averageScore);
    }
}
