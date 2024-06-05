package com.lt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KnowledgeTest {
    @Autowired
    private KnowledgeGraphConverter knowledgeGraphConverter;

    @Test
    void TOKnowledgeGraphConverter(){
        knowledgeGraphConverter.toKnowledgeGraphConverter();
        System.out.println("结束");
    }
}
