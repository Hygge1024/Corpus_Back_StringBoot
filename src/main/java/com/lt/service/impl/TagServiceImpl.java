package com.lt.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lt.doadmin.Tag;
import com.lt.service.TagService;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static String Tag_url;

    @Value("${strapi.url}")
    public void setTag_url(String url){
        Tag_url = url+"tags";
    }
    @Value("${strapi.AuthToken}")
    private String strapiAuthToken;

    @Override
    public List<Tag> findAll() {
        //发送GET请求并获得JSON数据
        System.out.println(Tag_url);
        String jsonResponse = restTemplate.getForObject(Tag_url,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            List<Tag> tagList = objectMapper.readValue(jsonResponse, new TypeReference<List<Tag>>() {
            });
            return tagList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int create(Tag tag) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Tag_url);
        //转换成json对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        try {
            String jsonBody = objectMapper.writeValueAsString(tag);
            //设置请求体为json格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);//请求体（格式）
            requestEntity.setContentType("application/json; charset=utf-8");
            httpPost.setEntity(requestEntity);
            //添加令牌到请求头
            httpPost.addHeader("Authorization", "Bearer " + strapiAuthToken);
            //发送请求并处理
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("statusCode值为：" + statusCode);
            if(statusCode == 200){
                return 1;
            }else {
                return 0;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(int tid) {
        String DeleteUrl = Tag_url + "/" + tid;
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(DeleteUrl);
        //添加授权令牌到请求头
        httpDelete.addHeader("Authorization", "Bearer " + strapiAuthToken);
        try {
            //发送DELETE请求
            HttpResponse response = httpClient.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return 1;
            } else {
                return 0;
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Tag tag) {
        int tid = tag.getId();
        String updateUrl = Tag_url + "/" + tid;
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(updateUrl);
        ObjectMapper objectMapper = new ObjectMapper();
        //设置驼峰命名
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        try {
            String jsonBody = objectMapper.writeValueAsString(tag);
            StringEntity requestEntity = new StringEntity(jsonBody,StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");
            httpPut.setEntity(requestEntity);
            httpPut.addHeader("Authorization", "Bearer " + strapiAuthToken);
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                return 1;
            }else {
                return 0;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
