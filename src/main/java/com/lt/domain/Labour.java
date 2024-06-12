package com.lt.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("labour")
public class Labour {
    @TableId(type = IdType.AUTO)
    private int labour_id;
    private int correct_id;
    private int total;
    private int coefficient;
    private Date startTime;
    private Date endTime;
    private double Information_one;
    private double Information_two;
    private double Information_three;
    private double Fluency_one;
    private double Fluency_two;
    private double Fluency_three;
    private double Grammer_one;
    private double Grammer_two;
    private double Grammer_three;
    private double Logical_one;
    private double Logical_two;
    private double Logical_three;
    private double Skill_one;
    private double Skill_two;
    private double Skill_three;

}
