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
}
