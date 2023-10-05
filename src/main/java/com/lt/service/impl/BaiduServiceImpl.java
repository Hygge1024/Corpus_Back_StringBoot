package com.lt.service.impl;

import com.lt.service.BaiduService;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class BaiduServiceImpl implements BaiduService {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Override
    public String Toriginaltext(String fileUrl, int direction, String BaiDuAPI_KEY, String BaiDuSECRET_KEY) throws IOException, InterruptedException {
        String result = new BaiduServiceImpl().getText(fileUrl, direction, BaiDuAPI_KEY, BaiDuSECRET_KEY);
        System.out.println("译文：" + result);
        return result;
    }

    public String getText(String audioUrl, int Direction, String API_KEY, String SECRET_KEY) throws IOException, InterruptedException {
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        MediaType mediaType = MediaType.parse("application/json");
        //翻译类型
        int pid = 0;
        if (Direction == 1) { //英译中，学生上传的音频为中文，识别中文
            pid = 80001;
        } else if (Direction == 2) {//中译英，识别英文
            pid = 1737;
        }
        // 创建请求体
        RequestBody body = RequestBody.create(mediaType, "{\"speech_url\":\"" + audioUrl + "\",\"format\":\"mp3\",\"pid\":" + pid + ",\"rate\":16000}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/aasr/v1/create?access_token=" + getAccessToken(API_KEY, SECRET_KEY))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        // 输出响应数据的Body内容
        String responseBody = response.body().string();
        // 解析JSON字符串
        JSONObject jsonResponse = new JSONObject(responseBody);
        //获取task_id的值
        String task_id = jsonResponse.getString("task_id");
        System.out.println("task_id: " + task_id);
        System.out.println("识别提交成功，下面开始获取识别文本内容");
        System.out.println("等待中...");
//      去获得take_id字段内容___________________________需要等待，直到翻译完有task_id值才开始进行
        boolean taskCompleted = false;
        System.out.println("开始尝试获取识别内容...");
        MediaType mediaType2 = MediaType.parse("application/json");
        RequestBody body2 = RequestBody.create(mediaType2, "{\"task_ids\":[\"" + task_id + "\"]}");
        Request request2 = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/aasr/v1/query?access_token=" + getAccessToken(API_KEY, SECRET_KEY))
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
//            System.out.println(responseBody2);
            // 解析JSON字符串
            JSONObject jsonResponse2 = new JSONObject(responseBody2);//将字符串转换成 JSON格式了
            //获取task_info数组
            JSONArray taskInfoArray = jsonResponse2.getJSONArray("tasks_info");
            //获取第一个任务信息对象
            JSONObject taskInfo = taskInfoArray.getJSONObject(0);
            // 检查任务状态
            String taskStatus = taskInfo.getString("task_status");
            if (taskStatus.equals("Success")) {
                taskCompleted = true;
                //获取task_result对象
                JSONObject taskResult = taskInfo.getJSONObject("task_result");
                //获取result数组
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
                break;//翻译失败，退出
            } else {
                // 任务未完成，继续轮询
                System.out.println("Translation task still in progress. Polling again in " + pollingInterval + " milliseconds.");
                Thread.sleep(pollingInterval);//进行睡眠等待
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

    //获取令牌
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
}
