package com.lt.service;

import java.io.IOException;

public interface BaiduService {
    //    教师语料（位于语料库） 音频识别翻译api:线上地址，类型，
    String Toriginaltext(String fileUrl, int direction, String BaiDuAPI_KEY, String BaiDuSECRET_KEY) throws IOException, InterruptedException;

//   学生练习 音频识别翻译api:线上地址，类型
//    String Toriginaltext(String fileUrl,int direction);
}
