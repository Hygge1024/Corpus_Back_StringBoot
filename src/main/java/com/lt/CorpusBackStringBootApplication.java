package com.lt;

import com.lt.api.ApiKeyInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CorpusBackStringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(CorpusBackStringBootApplication.class, args);
    }
}
