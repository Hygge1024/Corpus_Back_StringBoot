//package com.lt.strapi;
//
//import java.security.SecureRandom;
//import java.util.Base64;
//
//public class API密钥生成器 {
//    public static String generateApiKey(){
//        int ketLength = 32;//密钥的长度
//        //生成随机数
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] keyBytes = new byte[ketLength];
//        secureRandom.nextBytes(keyBytes);
//        //将随机数准换为Base64编码的字符串
//        String apiKey = Base64.getEncoder().encodeToString(keyBytes);
//        return apiKey;
//    }
//
//    public static void main(String[] args) {
//        String apiKey = generateApiKey();
//        System.out.println("生成的API密钥: " + apiKey);
//    }
//}
