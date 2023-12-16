package com.lt.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ExerciseScoreDao {
    private int id;
    private Date published_at;

    private float Information; //信息完整度分数 55%
    private float Fluency;//陈述流畅度分数 20%
    private float Grammar;//语法准确度 10%
    private float Logical;//逻辑连贯度 10%
    private float Pace;//语速 参考分数，不占比重
    private float Skill;//技巧灵活度 5%

    private float Volume;//音量
    private float AllScore;//总分
    private String TeacherComments;
}
