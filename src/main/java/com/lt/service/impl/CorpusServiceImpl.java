package com.lt.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lt.domain.*;
import com.lt.service.CorpusService;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CorpusServiceImpl implements CorpusService {
    private static final RestTemplate restTemplate = new RestTemplate();
    //    private static final String CorpusAll_url = "http://localhost:1337/corpuses";//上线时需要换成实际的地址和端口号 http://localhost:1337/corpuses
    //    private String CorpusOne_url;//上线时需要换成实际的地址和端口号 "http://localhost:1337/corpuses/"
    private static String CorpusAll_url;
    private static String CorpusOne_url;
    private static String strapiUploadUrl;

    @Value("${strapi.url}")
    public void setCorpusAll_url(String url) {
        CorpusAll_url = url + "corpuses";
    }

    @Value("${strapi.strapiUploadUrl}")
    public void setStrapiUploadUrl(String url) {
        strapiUploadUrl = url;
    }

    @Value("${strapi.AuthToken}")
    private String strapiAuthToken;

    //获取所有的语料资源
    @Override
    public List<Corpus> getAllCorpus() {
        //发送GET请求并获得JSON数据
        String jsonResponse = restTemplate.getForObject(CorpusAll_url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//将“未知属性异常”设置为false
        try {
            List<Corpus> corpusList = objectMapper.readValue(jsonResponse, new TypeReference<List<Corpus>>() {
            });
            return corpusList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);//都在直接抛出异常
        }
    }

    //获取指定的语料资源
    @Override
    public Corpus getOneCorpus(int cid) {
        CorpusOne_url = CorpusAll_url + "/";//这样做能保证url能唯一，不会出现重复的情况
        CorpusOne_url += cid;//更新后的url，能精确查找当个语料资源
        String jsonResponse = restTemplate.getForObject(CorpusOne_url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//将“未知属性异常”设置为false
        try {
            Corpus corpus = objectMapper.readValue(jsonResponse, Corpus.class);
            return corpus;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //分页查询
    @Override
    public List<Corpus> getPage(int currentPage, int pageSize) {
        List<Corpus> corpusList = new CorpusServiceImpl().getAllCorpus();
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, corpusList.size());
        return corpusList.subList(startIndex, endIndex);
    }

    @Override
    public List<Corpus> getByTnumber(int currentPage, int pageSize, String tnumber) {
        CorpusOne_url = CorpusAll_url + "?AuthorID=";
        CorpusOne_url += tnumber;
        String jsonResponse = restTemplate.getForObject(CorpusOne_url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//将“未知属性异常”设置为false
        try {
            List<Corpus> corpusList = objectMapper.readValue(jsonResponse, new TypeReference<List<Corpus>>() {
            });
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, corpusList.size());
            return corpusList.subList(startIndex, endIndex);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Corpus> getByTag_ids(int id) {
        String CorpusByTag_ids = CorpusAll_url + "?Tag_ids=" + id;
        String jsonResponse = restTemplate.getForObject(CorpusByTag_ids, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<Corpus> corpusList = objectMapper.readValue(jsonResponse, new TypeReference<List<Corpus>>() {
            });
            return corpusList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);//都在直接抛出异常
        }
    }


    @Override
    public List<Corpus> getByFactory(int currentPage, int pageSize, int Direction, int Difficulty, int Type, int Tag_ids, String Title_contains) {
        //先进性判断，是否为空，若不为空则添加，若为空则不管。=》最后平凑成完整的strapi的url格式
        System.out.println("这是getByFactory查询语句");
        System.out.println("参数信息为：" + Direction + Difficulty + Type + Tag_ids + Title_contains);
        String urlByFactory = CorpusAll_url + "?";
        if (Direction != 0) {
            urlByFactory += "Direction=" + Direction + "&";
        }
        if (Difficulty != 0) {
            urlByFactory += "Difficulty=" + Difficulty + "&";
        }
        if (Type != 0) {
            urlByFactory += "Type=" + Type + "&";
        }
        if (Tag_ids != 0) {
            urlByFactory += "Tag_ids=" + Tag_ids + "&";
        }
        if (Title_contains != null && !Title_contains.isEmpty()) {
            urlByFactory += "Title_contains=" + Title_contains;
        }
        System.out.println(urlByFactory);
        String jsonResponse = restTemplate.getForObject(urlByFactory, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<Corpus> corpusList = objectMapper.readValue(jsonResponse, new TypeReference<List<Corpus>>() {
            });
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, corpusList.size());
            return corpusList.subList(startIndex, endIndex);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);//都在直接抛出异常
        }
    }


    //将文件上传
    @Override
    public RcCorpus upload(MultipartFile file) {
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
                    String fileUrl = fileList.get(0).getUrl();
                    System.out.println(fileUrl);
                    Long fileId = fileList.get(0).getId();
                    System.out.println("这个返回的fileId = " + fileId);
                    RcCorpus rcCorpus = new RcCorpus();
                    rcCorpus.setFileUrl(fileUrl);
                    rcCorpus.setFileId(fileId);
                    return rcCorpus;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析JSON时出现异常: " + e.getMessage());
        }
    }

    //上传完整的语料资源
    @Override
    public int create(CorpusDao corpusDao) {//完整的语料信息需要调用上面上传文件返回的id值
        //添加时间属性
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        corpusDao.setPublished_at(currentDate);
        corpusDao.setCreated_at(currentDate);
        //调用strapi提供的API上传信息
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(CorpusAll_url);
        //将corpusDao对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(corpusDao);
            //设置请求体为JSON格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");//; charset=utf-8
            httpPost.setEntity(requestEntity);
            //添加令牌到请求头
            httpPost.addHeader("Authorization", "Bearer " + strapiAuthToken);
            //发送请求并处理
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
//            System.out.println("传递的JSON格式为：" + jsonBody);
//            System.out.println("statusCode值为：" + statusCode);
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
    public int update(CorpusDao corpus) {
        int cid = corpus.getId();
        String updateUrl = CorpusAll_url + "/" + cid;
//        System.out.println("更新路由url：" + updateUrl);
        // 创建HttpClient请求
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(updateUrl);
        // 将更新对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(corpus);
            // 设置请求体为JSON格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");
            httpPut.setEntity(requestEntity);
            // 添加授权令牌到请求头
            httpPut.addHeader("Authorization", "Bearer " + strapiAuthToken);
            // 发送PUT请求
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
//            System.out.println(statusCode);
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
    public int update2(CotpusDao2 corpus) {
        int cid = corpus.getId();
        String updateUrl = CorpusAll_url + "/" + cid;
//        System.out.println("更新路由url：" + updateUrl);
        // 创建HttpClient请求
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(updateUrl);
        // 将更新对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(corpus);
            // 设置请求体为JSON格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");
            httpPut.setEntity(requestEntity);
            // 添加授权令牌到请求头
            httpPut.addHeader("Authorization", "Bearer " + strapiAuthToken);
            // 发送PUT请求
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
//            System.out.println(statusCode);
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
    public int delete(int cid) {
        //完善删除url路径
        String DeleteUrl = CorpusAll_url + "/" + cid;
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

    @Override
    public List<Corpus> getSelf(int currentPage, int pageSize, String AuthorID, String Title, String created_at) {
        String urlBySelf = CorpusAll_url + "?";
        if (AuthorID != null && !AuthorID.isEmpty()) {
            urlBySelf += "AuthorID=" + AuthorID + "&";
        }
        if (Title != null && !Title.isEmpty()) {
            urlBySelf += "Title_contains=" + Title + "&";
        }
        if (created_at != null && !AuthorID.isEmpty()) {
            urlBySelf += "created_at=" + created_at;
        }

        System.out.println("url为" + urlBySelf);
        String jsonResponse = restTemplate.getForObject(urlBySelf, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        System.out.println("我的语料的json数据请求为： " + jsonResponse);
        try {
            List<Corpus> corpusList = objectMapper.readValue(jsonResponse, new TypeReference<List<Corpus>>() {
            });
//            System.out.println("数据个数为：" + corpusList.size());
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, corpusList.size());
            return corpusList.subList(startIndex, endIndex);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object readJson(String filePath, Class<?> valueType) throws IOException {
        Path path = Paths.get(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(path.toFile(), valueType);
    }


}