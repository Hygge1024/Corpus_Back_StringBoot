package com.lt.service;

import com.lt.domain.Corpus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendServiceTest {
    @Autowired
    private RecommendService recommendService;
    @Test
    public void test1(){
        List<Corpus> corpusList = recommendService.getRecommendByUserID("210001");
    }
}
