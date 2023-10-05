package com.lt.doadmin;

import lombok.Data;

import java.util.Date;

@Data
public class ExerciseScoreDao {
    private int id;
    private Date published_at;
    private float Fluency;
    private float Accuracy;
    private float Precision;
    private float Completeness;
    private float Pace;
    private float Volume;
    private float AllScore;
    private String TeacherComments;
}
