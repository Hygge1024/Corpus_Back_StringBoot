package com.lt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.domain.Labour;
import com.lt.domain.Exercises;
import com.lt.domain.teachers;
import com.lt.domain.TimeRange;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.domain.Exercises;

public interface LabourService extends IService<Exercises> {
    void createCorrection(int correctId, List<teachers> teachers);

    List<Exercises> queryExercisesByCorrectIdAndLabourId(int correctId, int labourId);

    int queryUncorrectedCount(int correctId, int labourId);

    TimeRange queryTimeRange(int labourId);

    void autoCorrect(int correctId, String WenXinAPI, String WenXinSecurity);

    double queryAverageScore(int correctId);
}
