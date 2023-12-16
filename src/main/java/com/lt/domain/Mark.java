package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Mark {
    @TableId(type = IdType.AUTO)
    int mid;
    int eid;
    String stunumber;
    String mtext;
    float mtime;
}
