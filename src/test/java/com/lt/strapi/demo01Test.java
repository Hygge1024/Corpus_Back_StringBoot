package com.lt.strapi;

/*
测试的部分，无关紧要，可以谁便删除
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lt.domain.Corpus;
import org.springframework.web.client.RestTemplate;

//@SpringBootTest
public class demo01Test {
    private static final RestTemplate restTemplate = new RestTemplate();
//    public demo01Test(RestTemplate restTemplate){
//        this.restTemplate = restTemplate;
//    }

    public static void main(String[] args) {
        String url = "http://localhost:1337/corpuses/7";
        //发送GET请求并获得JSON数据
        String jsonResponse = restTemplate.getForObject(url, String.class);
//        System.out.println(jsonResponse);//成功获取了json数据，只不过是String类型的
        //下一步，将String的json数据转换成对应的java对象
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Corpus corpus = objectMapper.readValue(jsonResponse, Corpus.class);
            System.out.println("语料标题：" + corpus.getTitle());
            System.out.println("语料介绍：" + corpus.getIntroduction());
            System.out.println("语料音频url地址：" + corpus.getFile().get(0).getUrl());
            System.out.println("Success !!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
