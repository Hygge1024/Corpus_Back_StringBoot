package com.lt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.lt.dao.ExercisesDao;
import com.lt.domain.Exercises;
import com.lt.domain.TimeRange;
import com.lt.domain.teachers;
import com.lt.service.LabourService;
import com.lt.service.TeachersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LabourServiceImpl extends ServiceImpl<ExercisesDao, Exercises> implements LabourService {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static String OriginalUrl;
    private static String ExercisesAll_Url;
    private static String ExercisesOne_Url;
    private static String strapiUploadUrl;

    @Value("${strapi.strapiUploadUrl}")
    public void setStrapiUploadUrl(String url) {
        strapiUploadUrl = url;
    }

    @Value("${strapi.AuthToken}")
    private String strapiAuthToken;

    @Value("${strapi.url}")
    public void setOriginalUrl(String url) {
        OriginalUrl = url;
    }

    public String getOriginalUrl() {
        return OriginalUrl;
    }

    @Value("${strapi.url}")
    public void setExercisesAll_Url(String url) {
        ExercisesAll_Url = url + "exercises";
    }

    @Autowired
    private ExercisesDao exercisesDao;

    @Autowired
    private TeachersService teachersService;

    private HttpClient httpClient = HttpClients.createDefault();

    @Override
    public void createCorrection(int correctId, List<teachers> teachers) {
        double factor = 0.1; // 确定选择100份
        int numExercises = (int) (10 * factor); // 选择100份

        // 查询符合条件的练习
        // String queryUrl = ExercisesAll_Url + "?Correctid=" + correctId;
        String queryUrl = ExercisesAll_Url + "?Correctid=" + correctId +
                "&Correcttype=0";

        log.info("Query URL: {}", queryUrl);
        String jsonResponse = restTemplate.getForObject(queryUrl, String.class);
        log.info("Received JSON response: {}", jsonResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<Exercises> exercises = objectMapper.readValue(jsonResponse,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Exercises.class));
            log.info("Parsed exercises: {}", exercises);

            if (exercises.isEmpty()) {
                log.error("No exercises found for correctId: {} and correctType: 0", correctId);
                throw new RuntimeException("No exercises found for the specified correctId and correctType.");
            }

            // 随机选择练习
            Random random = new Random();
            List<Exercises> selectedExercises = random.ints(0, exercises.size()).distinct().limit(numExercises)
                    .mapToObj(exercises::get).collect(Collectors.toList());
            log.info("Selected exercises: {}", selectedExercises);

            // 分配教师并更新练习状态
            for (int i = 0; i < selectedExercises.size(); i++) {
                Exercises exercise = selectedExercises.get(i);
                exercise.setTeaID(String.valueOf(teachers.get(i % teachers.size()).getTid())); // 转换为String类型

                // 调用API更新练习状态
                String updateUrl = ExercisesAll_Url + "/" + exercise.getId();
                HttpPut httpPut = new HttpPut(updateUrl);
                String jsonBody = objectMapper.writeValueAsString(exercise);
                StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
                requestEntity.setContentType("application/json; charset=utf-8");
                httpPut.setEntity(requestEntity);
                httpPut.addHeader("Authorization", "Bearer " + strapiAuthToken);
                HttpResponse response = httpClient.execute(httpPut);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    log.info("Updated exercise: {}", exercise);
                } else {
                    log.error("Failed to update exercise: {}", exercise);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: ", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Error executing HTTP request: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Exercises> queryExercisesByCorrectIdAndLabourId(int correctId, int labourId) {
        String queryUrl = ExercisesAll_Url + "?Correctid=" + correctId + "&Labourid=" + labourId;
        log.info("Query URL for exercises: {}", queryUrl);

        String jsonResponse = restTemplate.getForObject(queryUrl, String.class);
        log.info("Received JSON response for exercises: {}", jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<Exercises> exercises = objectMapper.readValue(jsonResponse,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Exercises.class));
            log.info("Parsed exercises: {}", exercises);
            return exercises;
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for exercises: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int queryUncorrectedCount(int correctId, int labourId) {
        String queryUrl = ExercisesAll_Url + "/count?Correctid=" + correctId + "&Labourid=" + labourId
                + "&Correcttype=4";
        log.info("Query URL for count: {}", queryUrl);

        String jsonResponse = restTemplate.getForObject(queryUrl, String.class);
        log.info("Received JSON response for count: {}", jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            Long count = jsonNode.asLong();
            log.info("Parsed count: {}", count);
            return count != null ? count.intValue() : 0;
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for count: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public TimeRange queryTimeRange(int labourId) {
        // 修改为通过外部服务获取 Exercises 数据
        String queryUrl = ExercisesAll_Url + "/" + labourId;
        String jsonResponse = restTemplate.getForObject(queryUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            Exercises exercise = objectMapper.readValue(jsonResponse, Exercises.class);
            if (exercise != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TimeRange timeRange = new TimeRange();
                if (exercise.getStartTime() != null) {
                    timeRange.setStartTime(dateFormat.format(exercise.getStartTime())); // 转换为String类型
                }
                if (exercise.getEndTime() != null) {
                    timeRange.setEndTime(dateFormat.format(exercise.getEndTime())); // 转换为String类型
                }
                return timeRange;
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: ", e);
        }
        return null;
    }

    @Override
    public void autoCorrect(int correctId, String WenXinAPI, String WenXinSecurity) {
        String queryUrl = ExercisesAll_Url + "?Correctid=" + correctId +
                "&Correcttype=0";
        // String queryUrl = ExercisesAll_Url + "?Correctid=" + correctId;
        String jsonResponse = restTemplate.getForObject(queryUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<Exercises> exercises = objectMapper.readValue(jsonResponse,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Exercises.class));
            log.info("查询结果: {}", exercises);

            // 调用智能批改接口进行批改
            for (Exercises exercise : exercises) {
                try {
                    String wenxinResult = teachersService.getWenxin_Value(exercise.getId(), WenXinAPI, WenXinSecurity);
                    float score = extractScoreFromWenxinResult(wenxinResult);
                    exercise.setScore(score);
                    exercise.setCorrecttype(3); // 设置为智能批改完成
                    exercisesDao.updateById(exercise);
                } catch (IOException e) {
                    log.error("调用智能批改API失败: ", e);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private float extractScoreFromWenxinResult(String result) {
        JSONObject jsonObject = new JSONObject(result);
        return (float) jsonObject.getDouble("总分值");
    }

    @Override
    public double queryAverageScore(int correctId) {
        String queryUrl = ExercisesAll_Url + "?Correctid=" + correctId;
        String jsonResponse = restTemplate.getForObject(queryUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<Exercises> exercises = objectMapper.readValue(jsonResponse,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Exercises.class));
            log.info("查询结果: {}", exercises);
            return exercises.stream()
                    .filter(ex -> ex.getCorrecttype() != 3)
                    .mapToDouble(Exercises::getScore)
                    .average()
                    .orElse(0.0);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
