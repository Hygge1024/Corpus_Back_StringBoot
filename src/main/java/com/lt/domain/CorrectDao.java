package com.lt.domain;

import lombok.Data;

import java.util.List;

@Data
public class CorrectDao {
    private String Title;// 语料标题
    private int Type;// 口译类型 1（同传） 2（交传） 3（陪同口译） 4（会议口译）
    private int Direction;// 翻译方向 1（中译英） 2（英译中）=>需要后端自己识别
//    private int Difficulty;// 翻译难度 1 （简单） 2（普通） 3（困难） 前端没有该属性，那就默认4
    private List<String> tag_ids;// 语料标签
    private String Originaltext;// 原文文本
    private String UserID;//上传者工号

    private String IdentifyText;//对应Exercise表的译文文本

}
