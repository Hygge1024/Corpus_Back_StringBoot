package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("two_level")
public class two_level {
    @TableId(type = IdType.AUTO)
    private int twoId;//主键ID
    private int exercisesId;//所属练习类exercises_id
    private int type;//1 表示属于Information的二级指标、2属于Fluency的二级指标、3属于Grammar的二级指标、4属于Logical的二级指标、5属于skills的二级指标
    private String twoComment;//评语
    private double twoScore;//二级指标的评分
    private String twoName;//二级指标自身的名称

}
