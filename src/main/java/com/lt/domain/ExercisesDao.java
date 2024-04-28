package com.lt.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExercisesDao {
    private int id;
    private String StuID;
    private int Corpus;
    private Date published_at;
    private Date created_at;
    private Long StuFile;
    // private int Score;//删除属性
    private String IdentifyText;
    private List<String> keywords; // 添加关键词列表属性

}
