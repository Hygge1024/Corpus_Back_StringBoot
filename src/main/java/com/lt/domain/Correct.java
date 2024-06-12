package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("correct")
public class Correct {
    @TableId(type = IdType.AUTO)
    private int correct_id;//试题批改id
    private int corpus_id;//语料id
    private int state;//1有效，0无效
}
