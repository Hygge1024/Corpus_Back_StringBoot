package com.lt.doadmin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class download {
    @TableId(type = IdType.AUTO)
    int did;
    int cid;
    String userid;
    String title;
    String urlfile;
    int who;//1代表学生，2代表教师，3代表管理员
}
