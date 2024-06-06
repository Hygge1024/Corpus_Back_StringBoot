package com.lt.service;

import java.io.IOException;
import java.util.List;

public interface BaiduService {
        // 教师语料（位于语料库） 音频识别翻译api:线上地址，类型，
        String Toriginaltext(String fileUrl, int direction, String BaiDuAPI_KEY, String BaiDuSECRET_KEY)
                        throws IOException, InterruptedException;

        // 学生练习 音频识别翻译api:线上地址，类型
        // String Toriginaltext(String fileUrl,int direction);
        String WenXin_Value(String contentA, String contentB, List<String> keywords, String WenXinAPI,
                        String WenXinSecurity, String fileUrl, int eid) throws IOException;
}
