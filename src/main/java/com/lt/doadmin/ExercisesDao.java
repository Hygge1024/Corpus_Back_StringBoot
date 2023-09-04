package com.lt.doadmin;

import lombok.Data;

import java.util.Date;

@Data
public class ExercisesDao {
    private int id;
    private String StuID;
    private int Corpus;
    private Date published_at;
    private Date created_at;
    private Long StuFile;
    private int Score;
    private String IdentifyText;

}
