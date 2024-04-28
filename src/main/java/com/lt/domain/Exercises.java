package com.lt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Exercises {
    @JsonProperty("id")
    private int id;
    @JsonProperty("StuID")
    private String StuID;
    @JsonProperty("published_at")
    private Date published_at;
    @JsonProperty("created_at")
    private Date created_at;
    @JsonProperty("updated_at")
    private Date updated_at;
    @JsonProperty("Corpus")
    private Corpus Corpus;
    @JsonProperty("AllScore")
    private float Score;
    @JsonProperty("IdentifyText")
    private String IdentifyText;
    @JsonProperty("StuFile")
    private List<FileData> StuFile;
    @JsonProperty("Fluency")
    private float Fluency;
    @JsonProperty("Information")
    private float Information;
    @JsonProperty("Grammar")
    private float Grammar;
    @JsonProperty("Logical")
    private float Logical;
    @JsonProperty("Pace")
    private float Pace;
    @JsonProperty("Volume")
    private float Volume;
    @JsonProperty("Skill")
    private float Skill;
    @JsonProperty("TeacherComments")
    private String TeacherComments;
    @JsonProperty("keywords") // 新增字段，用于存储关键词列表
    private List<String> keywords; // 使用List来直接操作字符串数组

}
