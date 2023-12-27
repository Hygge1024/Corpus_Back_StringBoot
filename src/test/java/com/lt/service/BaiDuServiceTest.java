package com.lt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class BaiDuServiceTest {
    @Autowired
    private BaiduService baiduService;

    @Test
    void testBaidu() throws IOException, InterruptedException {

        String end = baiduService.Toriginaltext("http://8.137.53.253:1337/uploads/dry_conditions_threaten_wild_horses_in_western_us_10230dd8ed.mp3", 2, "1uoRnTwzQAD9Zh2dniw90HIH", "b1dn5GiMqRglAGyGia23dD1nbXVb1Pri");
        System.out.println("结果为: " + end != null);
    }

    @Test
    void getWenXinResponse() throws IOException {
        String API_KEY = "8D9nuuAzMpfXUCSfbHX3rIsH";
        String SECRET_KEY = "mNrRXKGjE8vT6KphLEtXmKfCode5MwRT";
        String contentB = "与西方国家相比，中国成立博物馆的时间要晚得多，而且在过去，中国博物馆建设发展的速度是比较慢的。 中国的第一家公共博物馆是南通博物馆，该馆建立于1905年，位于现在华东地区江苏省的南通市。1949年11月，中国文化部成立了文化遗产管理局，开始对国内21家博物馆实施管理。此后，在全国各地陆续成立了各级博物馆管理机构。尤其是自1978年改革开放以来，随着国家总体实力的增强，中国的博物馆建设得到了迅猛的发展。";
        String contentA = "compared with western countries China set out much later to build its museums and the development of museums in the country was slow in the past,the first public museum of the country non two museum was founded in non tung a city of east china's John sue province in nineteen oh five,in November nineteen forty nine china's ministry of culture established the cultural relics administrative bureau which began management of the twenty one museums in the country,since then museum administrative authorities at different levels have been gradually established throughout the country especially after the start of the reform and opening up in nineteen seventy eight with the improvement of china's overall national strength Chinese museums have witnessed a more rapid development slashed begun developing more rapidly ";
        System.out.println(baiduService.WenXin_Value(contentA, contentB, API_KEY, SECRET_KEY));
    }
}
