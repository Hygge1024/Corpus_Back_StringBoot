package com.lt.api;

import java.lang.annotation.*;
/*
为了达到API密钥认证功能，自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiKeyRequired {
}