package com.lt.service;

import com.lt.domain.Mark;
import com.lt.domain.MarkUpDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MarkServiceTest {
    @Autowired
    private MarkService markService;

    @Test
    void getAll() {
        System.out.println(markService.getAll(1, "123"));
    }

    @Test
    void addMark() {
        Mark mark = new Mark();
        mark.setEid(1);
        mark.setStunumber("123");
        mark.setMtext("单元测试，还不错");
        float a = 13.33f;
        mark.setMtime(13.33F);
        System.out.println(markService.addMark(mark));
    }

    @Test
    void updateMark() {
        MarkUpDao mark = new MarkUpDao();
        mark.setMid(3);
        mark.setMtext("我是更新内容");
        System.out.println(markService.updateMark(mark));
    }

    @Test
    void deleteMark() {
        System.out.println(markService.deleteMark(3));
    }
}
