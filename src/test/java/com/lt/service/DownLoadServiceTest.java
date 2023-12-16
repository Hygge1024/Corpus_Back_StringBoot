package com.lt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lt.domain.download;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DownLoadServiceTest {
    @Autowired
    private DownLoadService downLoadService;

    @Test
    void getAll() {
        System.out.println(downLoadService.getAll(1, 2, "20230001", 1).getRecords());
    }

    @Test
    void getByTitle() {
        System.out.println(downLoadService.getByTitle(1, 2, "Cancer-treatment", "20230001", 1).getRecords());
    }

    @Test
    void insert() throws JsonProcessingException {
        download d = new download();
        d.setDid(2);
        d.setCid(11);
        d.setWho(1);
        d.setUserid("123");
        d.setUrlfile("dsdsd.mp3");
        d.setTitle("测试");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(d);

        System.out.println(json);
        System.out.println(downLoadService.insert(d));
    }

    @Test
    void delet() {
        System.out.println(downLoadService.delete(2));
    }
}
