package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class teachers {
    @TableId(type = IdType.AUTO)
    private int tid;
    private String tnumber;
    private String teaname;
    private String teapassword;
    private int tsex;
    private int teastate;
    private String teaphone;
}
