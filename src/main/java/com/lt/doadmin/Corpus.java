package com.lt.doadmin;

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
    private int Type;
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
    @JsonProperty("File")
    private List<FileData> File;
    @JsonProperty("Tag_ids")
    private List<Tag> Tag_ids;
}
