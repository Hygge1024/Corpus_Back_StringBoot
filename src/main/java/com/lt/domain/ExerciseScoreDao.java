package com.lt.domain;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ExerciseScoreDao {
    private int id;
    private Date published_at;

    private float Information; // 信息完整度分数 55%
    private float Fluency;// 陈述流畅度分数 20%
    private float Grammar;// 语法准确度 10%
    private float Logical;// 逻辑连贯度 10%
    private float Pace;// 语速 参考分数，不占比重
    private float Skill;// 技巧灵活度 5%

    private float Volume;// 音量
    private float AllScore;// 总分
    private String TeacherComments;
    @JsonProperty("InformationComment") // 新增字段
    private String informationComment;
    @JsonProperty("FluencyComment") // 新增字段
    private String fluencyComment; //
    @JsonProperty("GrammarComment") // 新增字段
    private String grammarComment;
    @JsonProperty("LogicalComment") // 新增字段
    private String logicalComment;
    @JsonProperty("SkillComment") // 新增字段
    private String skillComment;
}
