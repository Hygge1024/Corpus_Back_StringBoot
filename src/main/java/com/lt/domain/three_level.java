package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("three_level")
public class three_level {
    @TableId(type = IdType.AUTO)
    private int threeId;//主键ID
    private int twoId;//所属的二级指标
    private String threeComment;//评语
    private double threeScore;//三级指标的评分
    private String threeName;//三级指标自身的名称
}
