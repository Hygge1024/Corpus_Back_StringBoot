package com.lt.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/*
这个类主要是“上传”+"更新” 时调用，原因：这里面的属性是我strapi内容里面的。
 */
@Data
public class CorpusDao {
    private int id;// id
    private String Title;// 语料标题
    private String Introduction;// 语料简介
    private String Originaltext;// 原文文本
    private int Direction;// 翻译方向 1（中译英） 2（英译中）=>需要后端自己识别
    private int Difficulty;// 翻译难度 1 （简单） 2（普通） 3（困难）
    private int Type;// 口译类型 1（同传） 2（交传） 3（陪同口译） 4（会议口译）
    private String AuthorID;// 上传者id
    private Date published_at;// 上传时间
    private Date created_at;// 创建时间
    private int State;// 有效状态 1（有效） 2（无效） 模拟删除效果
    private int Published;// 练习发布状态，0 未发布、1 发布、 2 取消发布
    private Long File;// 音频文件id(视频or音频)
    private Long Picture;// 图像id
    private List<String> tag_ids;// 语料标签
    // private int amount;//该语料被练习次数
    private List<String> keywords;// 添加关键词数组字段
}
