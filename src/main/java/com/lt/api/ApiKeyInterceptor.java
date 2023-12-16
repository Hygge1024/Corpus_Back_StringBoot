package com.lt.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {
    private static String VALID_API_KEY;

    @Value("${api.key}")
    public void setKet(String key) {
        ApiKeyInterceptor.VALID_API_KEY = key;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 检查方法上是否有@ApiKeyRequired注解
            if (method.isAnnotationPresent(ApiKeyRequired.class)) {
//                System.out.println("需要密钥："+VALID_API_KEY);
                String apiKey = request.getHeader("Authorization");//获取请求中的API密钥
                //下面进行判断，（是否有效）
                if (apiKey != null && apiKey.equals("Bearer " + VALID_API_KEY)) {
                    //密钥有效，继续处理请求
//                    System.out.println("我被调用,请求中的api密钥为：" + apiKey);
                    return true;
                } else {
                    //密钥无效，返回未授权状态
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json;charset=UTF-8");
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> responseJson = new HashMap<>();
                    responseJson.put("code", 401); // 401 表示未授权状态
                    responseJson.put("msg", "Invalid API key!");
                    responseJson.put("data", null); // 将 "data" 字段设置为 null
                    response.getWriter().write(objectMapper.writeValueAsString(responseJson));
                    return false;
                }
            }
        }
        // 没有@ApiKeyRequired注解的方法不进行API密钥认证，直接放行
        return true;
    }
}
