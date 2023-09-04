package com.lt.doadmin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
//@TableName("students")         // 表名映射注解，当类名跟表名不一致时使用
public class students {
    @TableId(type = IdType.AUTO)
    private Integer sid;
//  @TableField("name")           // 列名映射注解，当属性名跟列名不一致时使用
    private String stunumber;
    private String stuname;
    private String stupassword;
    private String stuclass;
    private String stuphone;
    private int stusex;
    //表中没有的属性
    @TableField(exist = false)
    private int[] fid;//所有的收藏id
}
