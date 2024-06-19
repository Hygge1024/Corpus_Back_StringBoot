package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lt.dao.studentDao;
import com.lt.domain.*;
import com.lt.service.CorpusService;
import com.lt.service.ExercisesService;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExercisesServiceImpl implements ExercisesService {
    private static final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private CorpusService corpusService;
    @Autowired
    private studentDao studentDao;
    @Autowired
    private level_twoServiceImpl levelTwoService;
    @Autowired
    private level_threeServiceImpl levelThreeService;
    private static String OriginalUrl;
    private static String ExercisesAll_Url;
    private static String ExercisesOne_Url;
    private static String strapiUploadUrl;

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
        // System.out.println("输出结果为：" + jsonResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 将“未知属性异常”设置为false
        try {
            List<Exercises> exercisesList = objectMapper.readValue(jsonResponse, new TypeReference<List<Exercises>>() {
            });
            return exercisesList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);// 都在直接抛出异常
        }
    }

    @Override
    public List<Exercises> getAllExercisesHistory(String stuId) {
        String ExercisesAll_UrlDemo = ExercisesAll_Url + "?StuID=" + stuId;
        String jsonResponse = restTemplate.getForObject(ExercisesAll_UrlDemo, String.class);
        // System.out.println("输出结果为：" + jsonResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 将“未知属性异常”设置为false

        List<Corpus> corpusList = new ArrayList<>();

        try {
            List<Exercises> exercisesList = objectMapper.readValue(jsonResponse, new TypeReference<List<Exercises>>() {
            });
            log.info("排序前的exercisesList：");
            for (Exercises exercises : exercisesList) {
                log.info("排序前的eid：" + exercises.getId());
            }

            // 对 exercisesList 进行排序
            Collections.sort(exercisesList, new ExercisesComparator());

            log.info("排序后的exercisesList：");
            for (Exercises exercises : exercisesList) {
                log.info("排序后的eid：" + exercises.getId());
            }
            for (Exercises exercises : exercisesList) {
                log.info("当前的exercise的ID为" + exercises.getId());
                if (exercises.getCorpus().getId() != 0) {// 因为有的corpus被强制删除了
                    Corpus corpus = this.corpusService.getOneCorpus(exercises.getCorpus().getId());
                    exercises.getCorpus().setExercise_count(corpus.getExercise_count());
                } else {
                    exercises.getCorpus().setExercise_count(-1);// 表明该语料已经被删除了
                }

            }
            return exercisesList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);// 都在直接抛出异常
        }
    }

    @Override
    public Exercises getOneExercises(int eid) {
        ExercisesOne_Url = ExercisesAll_Url + "/";
        ExercisesOne_Url += eid;
        // System.err.println(ExercisesOne_Url);
        String jsonResponse = restTemplate.getForObject(ExercisesOne_Url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 将“未知属性异常”设置为false
        try {
            Exercises exercises = objectMapper.readValue(jsonResponse, Exercises.class);
            // 更新部分
            int cid = exercises.getCorpus().getId();
            List<Tag> tagsList = corpusService.getOneCorpus(cid).getTag_ids();
            exercises.getCorpus().setTag_ids(tagsList);
            return exercises;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Exercises> getPage(int currentPage, int pageSize, String stuId) {
        List<Exercises> exercisesList = new ExercisesServiceImpl().getAllExercises(stuId);

        // return exercisesList.subList(startIndex, endIndex);
        return exercisesList;
    }

    @Override
    public List<Exercises> getByCid(int cid) {
        ExercisesOne_Url = ExercisesAll_Url + "?Corpus=";
        ExercisesOne_Url += cid;
        // System.out.println(ExercisesOne_Url);
        String jsonResponse = restTemplate.getForObject(ExercisesOne_Url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 将“未知属性异常”设置为false
        try {
            List<Exercises> exercisesList = objectMapper.readValue(jsonResponse, new TypeReference<List<Exercises>>() {
            });

            return exercisesList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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
            ResponseEntity<String> response = restTemplate.exchange(strapiUploadUrl, HttpMethod.POST, requestEntity,
                    String.class);
            // 检查请求是否成功
            if (response.getStatusCode() == HttpStatus.OK) {
                // 解析JSON数据为FileData对象
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 将“未知属性异常”设置为false
                List<FileData> fileList = objectMapper.readValue(response.getBody(),
                        new TypeReference<List<FileData>>() {
                        });
                if (!fileList.isEmpty()) {
                    Long fileId = fileList.get(0).getId();
                    String fileUrl = fileList.get(0).getUrl();
                    System.out.println("url地址为" + fileUrl);
                    System.out.println("这个返回的fileId = " + fileId);
                    RcCorpus rcCorpus = new RcCorpus();
                    rcCorpus.setFileId(fileId);
                    rcCorpus.setFileUrl(fileUrl);
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

    @Override
    public int create(ExercisesDao exercises) {
        // 添加时间属性
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        exercises.setPublished_at(currentDate);
        exercises.setCreated_at(currentDate);
        // 调用strapi提供的API上传信息
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ExercisesAll_Url);
        // 将ExercisesDao对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(exercises);
            // 设置请求体为JSON格式
            StringEntity requestEntity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            requestEntity.setContentType("application/json; charset=utf-8");// ; charset=utf-8
            httpPost.setEntity(requestEntity);
            // 添加令牌到请求头
            httpPost.addHeader("Authorization", "Bearer " + strapiAuthToken);
            // 发送请求并处理
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 解析响应体
                org.apache.http.HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                    JsonNode responseJson = objectMapper.readTree(responseString);

                    // String res = EntityUtils.toString(entity);
                    // log.info("返回体为："+res);
                    JsonNode idNode = responseJson.get("id");
                    if (idNode != null) {
                        int id = idNode.asInt();
                        log.info("练习id为：" + id);
                        return id;
                    } else {
                        return 0;
                    }
                }
                return 0;
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
            System.out.println(statusCode); // !!!出现问题需要解决
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
    public int updateScore(ExerciseScoreDao exerciseScoreDao) {
        int eid = exerciseScoreDao.getId();
        String updateUrl = ExercisesAll_Url + "/" + eid;
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        float Pace = 0;
        float Volume = 0;
        /*
         * 这里需要添加接口用来实现 语速Pace、音量Volume 的自动识别
         */
        float ends[] = getautoScore(eid);
        Volume = ends[0]; // 音量
        Pace = ends[1]; // 语速
        // 流利度、标准度、准确度、完整度、语速、音量
        float AllScore = (float) ((float) exerciseScoreDao.getInformation() * 0.55 +
                (float) exerciseScoreDao.getFluency() * 0.20 +
                (float) exerciseScoreDao.getGrammar() * 0.10 +
                (float) exerciseScoreDao.getLogical() * 0.10 +
                (float) exerciseScoreDao.getSkill() * 0.05);

        exerciseScoreDao.setAllScore(AllScore);
        exerciseScoreDao.setPublished_at(currentDate);
        exerciseScoreDao.setVolume(Volume);
        exerciseScoreDao.setPace(Pace);
        // 创建HttpClient请求
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(updateUrl);
        // 将更新对象转换成JSON对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); // 设置属性名命名策略
        try {
            String jsonBody = objectMapper.writeValueAsString(exerciseScoreDao);
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
            log.info("这里问题了吗？");
            System.out.println(statusCode); // !!!出现问题需要解决
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
    public int updateLevelScore(NewExerciseLevelDao newExerciseLevelDao) {
        two_level twoLevel = newExerciseLevelDao.getTwoLevel();
        log.info("twolevel:"+twoLevel);
        List<three_level> threeLevelList = newExerciseLevelDao.getThreeLevelList();
        log.info("threeList:"+threeLevelList);
        int flag = 0;
        int two_id = levelTwoService.insert(twoLevel);//需要将返回的two_id 返回给下面的three_level值中
        flag = two_id;
        for(three_level threeLevel : threeLevelList){
            if(flag == 0){
                log.error("上传出错了！");
                return 0;
            }
            threeLevel.setTwoId(two_id);
            flag = levelThreeService.insert(threeLevel);
        }
        return 1;
    }

    @Override
    public List<NewExerciseLevelDao> getLevel(int exercises_id) {
        List<NewExerciseLevelDao> newExerciseLevelDaoList = new ArrayList<>();

        List<two_level> twoLevelList = levelTwoService.getall(exercises_id);
        for(two_level twoLevel : twoLevelList){
            List<three_level> threeLevelList = levelThreeService.getall(twoLevel.getTwoId());
            log.info("当前twoID："+twoLevel.getTwoId());
            NewExerciseLevelDao temple = new NewExerciseLevelDao();
            temple.setTwoLevel(twoLevel);
            temple.setThreeLevelList(threeLevelList);
            newExerciseLevelDaoList.add(temple);
        }
        return newExerciseLevelDaoList;
    }

    @Override
    public int delete(int eid) {
        // 完善删除url路径
        String DeleteUrl = ExercisesAll_Url + "/" + eid;
        // 创建HttpClient
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(DeleteUrl);// 添加删除url
        // 添加授权令牌到请求头
        httpDelete.addHeader("Authorization", "Bearer " + strapiAuthToken);
        try {
            // 发送DELETE请求
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

    // 单独的方法，用于计算音量+语速
    /*
     * 思路：
     * 通过eid向strapi发送请求，得到对应的练习资源（如 音频的url + identityText）
     * 音频的url：将资源格式转化成mav =》 通过算法获取 振幅+音量(db)
     * identityText（识别文本，不需要区分=》都识别） =》 通过算法获得的音频时长 计算 每分钟单词数，根据标准给出具体的得分
     * 注意：统计数字的部分可能有待加强
     */
    @Override
    public float[] getautoScore(int eid) {
        float[] ends = new float[2];
        Exercises exercises = this.getOneExercises(eid);
        // 识别文本：用于计算语速
        String IdentifyText = exercises.getIdentifyText();// 识别文本（学生上传的 翻译音频识别的结果）
        List<FileData> fileDataList = exercises.getStuFile();
        // int Direction = exercises.getCorpus().getDirection();//翻译方向，1英翻中，2中翻英
        int textCount = countWords(IdentifyText) + countChineseCharacters(IdentifyText);// 文字数量，全跑一边，统计所有
        // 文件路径：计算音量+音频时长
        String filePath = OriginalUrl.substring(0, OriginalUrl.length() - 1) + fileDataList.get(0).getUrl();
        // 先对音频进行格式转换
        String outputFilePath;
        // = "src/main/resources/static/temp.wav";//零时的文件——运行结束就删除了_本地
        /*
         * 针对上面：jar包中无相对路径——解决方案
         * 创建零时文件，并获得其路径
         */
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".wav");
            outputFilePath = tempFile.getPath();
            System.out.println("临时路径为：" + outputFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (filePath.toLowerCase().endsWith(".mp3")) {
            convertMP3toWAV(filePath, outputFilePath);
        } else {
            System.out.println("不支持的音频格式,返回0值");
            ends[0] = 0;
            ends[1] = 0;
            return ends;
        }
        // 获取音频时长
        double TimeLength = getLength(outputFilePath);
        // 获取音频音量信息
        double db = calculateAmplitudeAndVolume(outputFilePath);// 计算分贝值
        double Volume = scoreVolume(db);
        double Speed = textCount / TimeLength;// 计算语速 字/每秒
        double Pace = scoreTranslationSpeed(Speed);
        // 删除输出文件
        File outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            try {
                if (outputFile.delete()) {
                    System.out.println("输出文件已删除");
                } else {
                    System.err.println("无法删除输出文件——正常");
                }
            } catch (SecurityException e) {
                System.err.println("没有足够的权限来删除文件: " + e.getMessage());
            }
        }
        ends[0] = (float) Volume;
        ends[1] = (float) Pace;
        // 输出查看区域
        System.out.println("完整的音频路径： " + filePath);
        System.out.println("文字总数量为：" + textCount);
        System.out.println("音量db： " + Volume);
        System.out.println("音频时长： " + TimeLength);
        System.out.println("语速:" + ends[1] + "字/秒");
        return ends;// 返回值为int[] 数组格式，语速+音量

    }

    @Override
    public List<Exercises> getExerciseByClass(String stuclas) {
        // 先通过班级查询所有的学生
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stuclass", stuclas);
        List<students> studentsList = studentDao.selectList(wrapper);
        log.info("班级查询到的学生有：" + studentsList);
        // 再通过学生stuid查询exercises表查询所有满足条件的练习 -> 练习整合后返回给前端
        List<Exercises> exercisesList = new ArrayList<>();
        QueryWrapper wrapper1 = new QueryWrapper();
        for (students stu : studentsList) {
            List<Exercises> exercisesList1 = this.getAllExercises(stu.getStunumber());
            log.info("班级" + stuclas + "里的同学" + stu.getStunumber() + "的练习有：" + exercisesList1);
            exercisesList.addAll(exercisesList1);
        }
        return exercisesList;
    }

    @Override
    public List<Exercises> getExerciseSubmissionsByClassAndExercise(String classname, int exerciseId) {
        // 先通过班级查询所有的学生
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stuclass", classname);
        List<students> studentsList = studentDao.selectList(wrapper);
        log.info("班级查询到的学生有：" + studentsList);
        // 再通过学生stuid查询exercises表查询所有满足条件的练习 -> 练习整合后返回给前端
        List<Exercises> filteredExercises = new ArrayList<>();
        for (students stu : studentsList) {
            List<Exercises> exercisesList = this.getAllExercises(stu.getStunumber());
            // 筛选出对应特定练习ID的练习
            List<Exercises> specificExercises = exercisesList.stream()
                    .filter(exercise -> exercise.getCorpus().getId() == exerciseId)
                    .collect(Collectors.toList());
            log.info("班级" + classname + "里的同学" + stu.getStunumber() + "提交的练习ID为" + exerciseId + "的练习有："
                    + specificExercises);
            filteredExercises.addAll(specificExercises);
        }
        return filteredExercises;
    }

    // 统计中文字数
    public static int countChineseCharacters(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isChineseCharacter(c)) {
                count++;
            }
        }
        return count;
    }

    // 统计英文字数
    public static int countWords(String text) {
        // 使用正则表达式匹配英文单词，包括单引号和逗号内的单词
        Pattern pattern = Pattern.compile("\\b[\\w']+\\b|\\b[\\w]+,[\\w']+[\\w]*\\b");
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static boolean isChineseCharacter(char c) {
        return '\u4e00' <= c && c <= '\u9fff';
    }

    // 转换音频格式的函数
    // MP3文件转换成mav格式
    public static void convertMP3toWAV(String mp3FilePath, String wavFilePath) {
        try {
            // 创建临时文件
            File tempMp3File = File.createTempFile("temp", ".mp3");

            // 下载远程MP3文件到临时文件
            try (InputStream in = new URL(mp3FilePath).openStream();
                    OutputStream out = new FileOutputStream(tempMp3File)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            // 执行转换，传递临时MP3文件的路径
            Converter converter = new Converter();
            converter.convert(tempMp3File.getPath(), wavFilePath);
            // 删除临时MP3文件
            if (tempMp3File.exists()) {
                if (tempMp3File.delete()) {
                    System.out.println("删除临时的MP3文件成功");
                } else {
                    System.err.println("删除临时的MP3文件失败");
                }

            }
            System.out.println("Mp3格式转换成WAV完成，wav文件还在");
        } catch (JavaLayerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 获取音频音量函数
    public static double calculateAmplitudeAndVolume(String filaPath) {
        try {
            // 获取音频文件的音频输入流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filaPath));
            // 获取音频格式
            AudioFormat format = audioInputStream.getFormat();
            // 创建缓冲区以读取音频数据
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalSquaredAmplitude = 0;// 总振幅的平方和
            long sampleCount = 0;// 样本计数
            // 读取音频数据并计算总振幅的平方和
            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i += format.getFrameSize()) {
                    int sample = 0;
                    // 将字节数据合并为整数样本
                    for (int j = 0; j < format.getFrameSize(); j++) {
                        sample |= buffer[i + j] << (j * 8);
                    }
                    // 计算振幅的平方并累加
                    totalSquaredAmplitude += sample * sample;
                    sampleCount++;
                }
            }
            // 计算均方根（RMS）振幅
            double meanSquaredAmplitude = (double) totalSquaredAmplitude / sampleCount;
            double rmsAmplitude = Math.sqrt(meanSquaredAmplitude);
            // 将RMS振幅转换成分贝单位db
            double db = 20 * Math.log10(rmsAmplitude);
            audioInputStream.close();
            System.out.println("音量（分贝）计算完成");
            return db;
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 获取音频时长函数
    public static double getLength(String filaPath) {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filaPath));
            long audioFileLength = audioInputStream.getFrameLength();
            float frameRate = audioInputStream.getFormat().getFrameRate();
            double durationInSeconds = (double) audioFileLength / frameRate;
            audioInputStream.close();
            System.out.println("音频文件时长计算完成");
            return durationInSeconds;
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 计算语速分值
    public static float scoreTranslationSpeed(double wordsPerSecond) {
        if (wordsPerSecond > 12.0) {
            return 100;
        } else if (wordsPerSecond >= 10.0) {
            return (int) (90 + (wordsPerSecond - 10.0) * 5);
        } else if (wordsPerSecond >= 8.0) {
            return (int) (80 + (wordsPerSecond - 8.0) * 5);
        } else if (wordsPerSecond >= 6.0) {
            return (int) (70 + (wordsPerSecond - 6.0) * 5);
        } else if (wordsPerSecond >= 4.0) {
            return (int) (60 + (wordsPerSecond - 4.0) * 5);
        } else if (wordsPerSecond >= 2.0) {
            return (int) (50 + (wordsPerSecond - 2.0) * 5);
        } else {
            return 50;
        }
    }

    // 计算音量分值
    public static float scoreVolume(double decibels) {
        if (decibels >= 70.0) {
            return 100;
        } else if (decibels >= 65.0) {
            return (int) (90 + (decibels - 65.0) * 2);
        } else if (decibels >= 60.0) {
            return (int) (80 + (decibels - 60.0) * 2);
        } else if (decibels >= 55.0) {
            return (int) (70 + (decibels - 55.0) * 2);
        } else if (decibels >= 50.0) {
            return (int) (60 + (decibels - 50.0) * 2);
        } else {
            return 50;
        }
    }
    // 对语进行created_at排序

    public class ExercisesComparator implements Comparator<Exercises> {
        @Override
        public int compare(Exercises o1, Exercises o2) {
            // 比较 o1 和 o2 的 created_at 字段
            return o2.getCreated_at().compareTo(o1.getCreated_at()); // 降序排序
        }
    }

}
