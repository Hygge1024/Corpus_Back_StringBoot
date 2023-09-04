package com.lt.doadmin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("classes")
public class Classes {
    @TableId(type = IdType.AUTO)
    private int cid;
    private String cname;
}
