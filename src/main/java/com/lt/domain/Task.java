package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("task")
public class Task {
    @TableId(type = IdType.AUTO)
    private int tsid;//发布练习的主键id
    private int corpusid;//语料所对应的id
    private String classname;//发布的班级id
    private int state; //发布的状态，1 发布、2 取消发布
    private Date publishtime;//发布时间
    private String taskname;//练习的名称（默认和语料的标题一致）
    private String teanumber;
    @TableField(exist = false)
    private Corpus corpus;

}
