package com.lt.service.impl;

import com.lt.service.BaiduService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class BaiduServiceImpl implements BaiduService {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    /**
     * 音频转文本
     *
     * @param fileUrl         文件地址
     * @param direction       转译方向
     * @param BaiDuAPI_KEY    百度API
     * @param BaiDuSECRET_KEY 密钥
     * @return 返回转译文本
     * @throws IOException          IO异常
     * @throws InterruptedException 异常
     */
    @Override
    public String Toriginaltext(String fileUrl, int direction, String BaiDuAPI_KEY, String BaiDuSECRET_KEY)
            throws IOException, InterruptedException {
        String result = new BaiduServiceImpl().getText(fileUrl, direction, BaiDuAPI_KEY, BaiDuSECRET_KEY);
        System.out.println("译文：" + result);
        return result;
    }

    /**
     * 文心自动化评测
     *
     * @param contentA 学生的文本
     * @param contentB 语料原文
     * @return 评价内容
     */
    @Override
    public String WenXin_Value(String contentA, String contentB, List<String> keywords, String WenXinAPI,
            String WenXinSecurity) throws IOException {
        // 将关键词列表转换为字符串，用于加入到Prompt中
        String keywordsString = String.join(",", keywords);

        // 获取评分的提示字符串
        String promptString = GetPrompt(contentA, contentB, keywordsString);
        return new BaiduServiceImpl().getWenXinResponse(promptString, WenXinAPI, WenXinSecurity);
    }

    public String getText(String audioUrl, int Direction, String API_KEY, String SECRET_KEY)
            throws IOException, InterruptedException {
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        MediaType mediaType = MediaType.parse("application/json");
        // 翻译类型
        int pid = 0;
        if (Direction == 1) { // 英译中，学生上传的音频为中文，识别中文
            pid = 80001;
        } else if (Direction == 2) {// 中译英，识别英文
            pid = 1737;
        }
        // 创建请求体
        RequestBody body = RequestBody.create(mediaType,
                "{\"speech_url\":\"" + audioUrl + "\",\"format\":\"mp3\",\"pid\":" + pid + ",\"rate\":16000}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/aasr/v1/create?access_token="
                        + getAccessToken(API_KEY, SECRET_KEY))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        // 输出响应数据的Body内容
        String responseBody = response.body().string();
        // 解析JSON字符串
        JSONObject jsonResponse = new JSONObject(responseBody);
        // 获取task_id的值
        String task_id = jsonResponse.getString("task_id");
        System.out.println("task_id: " + task_id);
        System.out.println("识别提交成功，下面开始获取识别文本内容");
        System.out.println("等待中...");
        // 去获得take_id字段内容___________________________需要等待，直到翻译完有task_id值才开始进行
        boolean taskCompleted = false;
        System.out.println("开始尝试获取识别内容...");
        MediaType mediaType2 = MediaType.parse("application/json");
        RequestBody body2 = RequestBody.create(mediaType2, "{\"task_ids\":[\"" + task_id + "\"]}");
        Request request2 = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/aasr/v1/query?access_token="
                        + getAccessToken(API_KEY, SECRET_KEY))
                .method("POST", body2)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        String responseBody2 = null;
        // 轮询间隔时间（毫秒）
        int pollingInterval = 5000; // 5秒
        String result = "失败";
        while (!taskCompleted) {
            Response response2 = HTTP_CLIENT.newCall(request2).execute();
            responseBody2 = response2.body().string();
            // System.out.println(responseBody2);
            // 解析JSON字符串
            JSONObject jsonResponse2 = new JSONObject(responseBody2);// 将字符串转换成 JSON格式了
            // 获取task_info数组
            JSONArray taskInfoArray = jsonResponse2.getJSONArray("tasks_info");
            // 获取第一个任务信息对象
            JSONObject taskInfo = taskInfoArray.getJSONObject(0);
            // 检查任务状态
            String taskStatus = taskInfo.getString("task_status");
            if (taskStatus.equals("Success")) {
                taskCompleted = true;
                // 获取task_result对象
                JSONObject taskResult = taskInfo.getJSONObject("task_result");
                // 获取result数组
                JSONArray resultArray = taskResult.getJSONArray("result");
                // 提取result中的值
                StringBuilder resultBuilder = new StringBuilder();
                for (int i = 0; i < resultArray.length(); i++) {
                    String sentence = resultArray.getString(i);
                    resultBuilder.append(sentence).append(" ");
                }
                result = resultBuilder.toString().trim();
                System.out.println("获取识别文本完成");
                System.out.println("操作成功");
            } else if (taskStatus.equals("Failed") || taskStatus.equals("Timeout")) {
                System.out.println("Translation task failed or timed out.");
                break;// 翻译失败，退出
            } else {
                // 任务未完成，继续轮询
                System.out.println(
                        "Translation task still in progress. Polling again in " + pollingInterval + " milliseconds.");
                Thread.sleep(pollingInterval);// 进行睡眠等待
            }

        }
        // 记录结束时间
        long endTime = System.currentTimeMillis();
        // 计算经过的时间（以毫秒为单位）
        long elapsedTime = endTime - startTime;
        // 将毫秒转换为小时、分钟和秒
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        // 计算剩余的分钟和秒
        minutes = minutes % 60;
        seconds = seconds % 60;
        System.out.println("经过的时间：" + hours + "小时 " + minutes + "分钟 " + seconds + "秒");
        return result;
    }

    public String getWenXinResponse(String PromptString, String WenXinAPI, String WenXinSecurity) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"messages\":[{\"role\":\"user\",\"content\":\"" + PromptString + "\"}]}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token="
                        + getAccessToken(WenXinAPI, WenXinSecurity))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String ends = response.body().string();
        JSONObject jsonObject = new JSONObject(ends);
        // log.info("jsonObject:" + jsonObject);
        // 获取result字段的值
        String resultValue = jsonObject.getString("result");
        return resultValue;
    }

    // 获取令牌
    static String getAccessToken(String API_KEY, String SECRET_KEY) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

    // 文心的指令Prompt
    public String GetPrompt(String contentA, String contentB, String contentC) {
        String evaluation = "1. 信息完整度 (Information Completeness): 55%" +
                "  - 解释： 信息完整度衡量学生是否能够准确、全面地传达原文中的所有关键信息。这包括确保重要概念、细节和上下文得以正确表达。" +
                "  - 评分方法： 老师在0到100分的范围内打分，然后将得分乘以0.55，以反映这一方面在总分中的相对重要性。" +
                "2. 陈述流畅度 (Fluency of Expression): 20%" +
                "  - 解释： 陈述流畅度关注口译的表达是否自然、连贯、流畅，以确保译文在语言上具有自然的节奏和易于理解。" +
                "  - 评分方法： 老师在0到100分的范围内打分，然后将得分乘以0.20，以反映这一方面在总分中的相对权重。" +
                "3. 语法准确度 (Grammar Accuracy): 10%" +
                "  - 解释： 语法准确度评估口译中语法结构和用词的正确性，确保学生能够在表达中避免语法错误。" +
                "  - 评分方法： 老师在0到100分的范围内打分，然后将得分乘以0.10，以反映这一方面在总分中的相对权重。" +
                "4. 逻辑连贯度 (Logical Coherence): 10%" +
                "  - 解释： 逻辑连贯度关注学生口译是否能够呈现出清晰、有条理的思维结构，确保译文在逻辑上具有一致性和合理性。" +
                "  - 评分方法： 老师在0到100分的范围内打分，然后将得分乘以0.10，以反映这一方面在总分中的相对权重。" +
                "5. 技巧灵活度 (Skill Flexibility): 5%" +
                "  - 解释： 技巧灵活度评估学生在口译过程中是否展现出一些创新和灵活性，包括对不同语境的适应能力和选择合适表达方式的技巧。" +
                "  - 评分方法： 老师在0到100分的范围内打分，然后将得分乘以0.05，以反映这一方面在总分中的相对权重。";

        String PromptString = "你现在是一名专业的英语口译方面的专家，下面是同学A口译后的文本内容:" + contentA +
                "+请与标准原文:" + contentB + ",进行对比分析,其中需要重点关注关键词" + contentC + ",下面是评分准则口译员评分准则:"
                + evaluation +
                "背景信息:同学A对一段英文音频进行中文的口译后，需要对其进行评价、评分。" +
                "补充数据:信息完整度占55%，陈述流畅度占20%，语法准确度占10%，逻辑连贯度占10%，技巧灵活度占5%,总分值为100分。" +
                "输出格式:给出最终评分的值，满分100分,分值范围0-100。其中信息完整度占55%，陈述流畅度占20%，语法准确度占10%，逻辑连贯度占10%，技巧灵活度占5%。";
        return PromptString;
    }
}
