package com.lt.service;

import com.lt.doadmin.Corpus;
import com.lt.service.impl.CorpusServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SpringBootTest
public class CorpusServiceTest {
    @Autowired
    private CorpusServiceImpl corpusService;

    @Test
    void getAllCorpus() {
        List<Corpus> corpusList = corpusService.getAllCorpus();
        System.out.println(corpusList);
    }

    @Test
    void getOneCorpus() {
        int cid = 34;
        Corpus corpus = corpusService.getOneCorpus(cid);
        System.out.println(corpus.getDirection());
    }

    @Test
    void getPage() {
        int currentPage = 1;
        int pageSize = 3;
        List<Corpus> corpusList = corpusService.getPage(currentPage, pageSize);
        System.out.println(corpusList);
    }

    //    @Test
//    void upload(){
//        Corpus_self corpusSelf = new Corpus_self();
//        System.out.println(corpusService.upload(corpusSelf));
//    }
    @Test
    void getByCorpus() {
        String dateString = "2023-07-13T16:50:53.000Z";
        List<Corpus> corpusList = corpusService.getSelf(1, 2, "1996", "New", dateString);
        System.out.println("结果为");
        System.out.println(corpusList);

    }

    @Test
    void getByTnumber() {
        System.out.println(corpusService.getByTnumber(1, 2, "1995"));
    }

}
