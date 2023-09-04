package com.lt.doadmin;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class teaclass {
    int cid;
    String tnumber;
    @TableField(exist = false)
    String cname;//班级名称
}
