package com.lt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Favorites {
    @TableId(type = IdType.AUTO)
    private int fid;
    private int cid;
    private String ctitle;
    private int who;//who等于1（学生）2（教师）3（管理员——不确定是否需要给管理员添加）
    private String userid;
    private int tagids;
}
