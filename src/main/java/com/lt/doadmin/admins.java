package com.lt.doadmin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class admins {
    @TableId(type = IdType.AUTO)
    private int aid;
    private String anumber;
    private String admname;
    private String admpassword;
    private int asex;
}
