package com.lt.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lt.doadmin.Corpus;
import com.lt.doadmin.Exercises;
import com.lt.doadmin.ExercisesDao;
import com.lt.doadmin.FileData;
import com.lt.service.ExercisesService;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ExercisesServiceImpl implements ExercisesService {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static String ExercisesAll_Url;
    private static String ExercisesOne_Url;
    private static String strapiUploadUrl;

    @Value("${strapi.url}")
    public void setExercisesAll_Url(String url) {
        ExercisesAll_Url = url + "exercises";
    }

    @Value("${strapi.strapiUploadUrl}")
    public void setStrapiUploadUrl(String url) {
        strapiUploadUrl = url;
    }

    @Value("${strapi.AuthToken}")
    private String strapiAuthToken;

    @Override
    public List<Exercises> getAllExercises(String stuId) {
        String ExercisesAll_UrlDemo = ExercisesAll_Url + "?StuID=" + stuId;
        String jsonResponse = restTemplate.getForObject(ExercisesAll_UrlDemo, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//将“未知属性异常”设置为false
        try {
            List<Exercises> exercisesList = objectMapper.readValue(jsonResponse, new TypeReference<List<Exercises>>() {
            });
            return exercisesList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);//都在直接抛出异常
        }
    }

    @Override
    public Exercises getOneExercises(int eid) {
        ExercisesOne_Url = ExercisesAll_Url + "/";
        ExercisesOne_Url += eid;
//        System.out.println(ExercisesOne_Url);
        String jsonResponse = restTemplate.getForObject(ExercisesOne_Url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//将“未知属性异常”设置为false
        try {
            Exercises exercises = objectMapper.readValue(jsonResponse, Exercises.class);
            return exercises;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Exercises> getPage(int currentPage, int pageSize, String stuId) {
        List<Exercises> exercisesList = new ExercisesServiceImpl().getAllExercises(stuId);
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, exercisesList.size());
        return exercisesList.subList(startIndex, endIndex);
    }

    @Override
    public Long upload(MultipartFile file) {
        try {
            // 准备包含文件的多部分请求
            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            bodyMap.add("files", file.getResource());

            // 准备带有认证令牌的请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(strapiAuthToken);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
            // 发送POST请求将文件上传至Strapi
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(strapiUploadUrl, HttpMethod.POST, requestEntity, String.class);
            // 检查请求是否成功
            if (response.getStatusCode() == HttpStatus.OK) {
                //解析JSON数据为FileData对象
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//将“未知属性异常”设置为false
                List<FileData> fileList = objectMapper.readValue(response.getBody(), new TypeReference<List<FileData>>() {
                });
                if (!fileList.isEmpty()) {
                    Long fileId = fileList.get(0).getId();
                    System.out.println("这个返回的fileId = " + fileId);
                    return fileId;
                } else {
                    return 0L;
                }
            } else {
                return 0L;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析JSON时出现异常: " + e.getMessage());
        }
    }

    @Override
    public int create(ExercisesDao exercises) {
        //添加时间属性
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        exercises.setPublished_at(currentDate);
        exercises.setCreated_at(currentDate);
        //调用strapi提供的API上传信息
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ExercisesAll_Url);
        //将ExercisesDao对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(exercises);
            //设置请求体为JSON格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");//; charset=utf-8
            httpPost.setEntity(requestEntity);
            //添加令牌到请求头
            httpPost.addHeader("Authorization", "Bearer " + strapiAuthToken);
            //发送请求并处理
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return 1;
            } else {
                return 0;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(ExercisesDao exercisesself) {
        int eid = exercisesself.getId();
        String updateUrl = ExercisesAll_Url + "/" + eid;
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        exercisesself.setPublished_at(currentDate);
        // 创建HttpClient请求
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(updateUrl);
        // 将更新对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(exercisesself);
            System.out.println(jsonBody);
            // 设置请求体为JSON格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");
            httpPut.setEntity(requestEntity);
            // 添加授权令牌到请求头
            httpPut.addHeader("Authorization", "Bearer " + strapiAuthToken);
            // 发送PUT请求
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode); //!!!出现问题需要解决
            if (statusCode == 200) {
                return 1;
            } else {
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
    public int delete(int eid) {
        //完善删除url路径
        String DeleteUrl = ExercisesAll_Url + "/" + eid;
        //创建HttpClient
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(DeleteUrl);//添加删除url
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
}
