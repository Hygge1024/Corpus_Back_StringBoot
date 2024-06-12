package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("labour_teacher")
public class labour_teacher {
    @TableId(type = IdType.AUTO)
    private int id;
    private int labour_id;
    private int teacher_id;
}
