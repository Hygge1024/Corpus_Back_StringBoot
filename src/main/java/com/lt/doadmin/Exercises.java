package com.lt.doadmin;

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
    @JsonProperty("Score")
    private int Score;
    @JsonProperty("IdentifyText")
    private String IdentifyText;
    @JsonProperty("StuFile")
    private List<FileData> StuFile;
}
