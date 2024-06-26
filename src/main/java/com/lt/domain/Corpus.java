package com.lt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/*
这个类主要是用于“查询”功能=>将对应的值（属性为要展示的值+@JsonProperty属性 才能让结果与其匹配）
 */
@Data
public class Corpus {
    @JsonProperty("id")
    private int id;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Introduction")
    private String Introduction;
    @JsonProperty("Originaltext")
    private String Originaltext;
    @JsonProperty("Direction")
    private int Direction;
    @JsonProperty("Difficulty")
    private int Difficulty;
    @JsonProperty("Type")
    private int Type;// 口译类型： 1 同传 2 交传 3 会议口译 4 陪同口译
    @JsonProperty("AuthorID")
    private String AuthorID;
    @JsonProperty("published_at")
    private Date published_at;
    @JsonProperty("created_at")
    private Date created_at;
    @JsonProperty("updated_at")
    private Date updated_at;
    @JsonProperty("State")
    private int State;
    @JsonProperty("Published")
    private int Published;
    @JsonProperty("File")
    private List<FileData> File;
    @JsonProperty("Tag_ids")
    private List<Tag> Tag_ids;
    @JsonProperty("Picture")
    private List<PictureData> Picture;
    // @JsonIgnore
    private int exercise_count;// 该语料被练习次数（主要服务与首页查询）
    @JsonProperty("keywords")
    private List<String> keywords; // 用来.存储与语料相关的关键词列表
    @JsonProperty("DurationEstimateErrorPercentage")
    private double durationEstimateErrorPercentage; // 存储误差百分比
    @JsonProperty("TextScorePercentage")
    private double textScorePercentage; // 文本成绩占比
    @JsonProperty("AudioScorePercentage")
    private double audioScorePercentage; // 音频成绩占比
}
