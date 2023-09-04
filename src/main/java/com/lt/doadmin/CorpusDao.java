package com.lt.doadmin;

import lombok.Data;

import java.util.Date;
import java.util.List;
/*
这个类主要是“上传”+"更新” 时调用，原因：这里面的属性是我strapi内容里面的。
 */
@Data
public class CorpusDao {
    private int id;
    private String Title;
    private String Introduction;
    private String Originaltext;
    private int Direction;
    private int Difficulty;
    private int Type;
    private String AuthorID;
    private Date published_at;
    private Date created_at;
    private int State;
    private Long File;
    private List<String> tag_ids;
}
