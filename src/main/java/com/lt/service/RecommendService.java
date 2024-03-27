package com.lt.service;

import com.lt.domain.Corpus;

import java.util.List;

/**
 * 实现推荐算法的具体接口类——基于内容的推荐
 */
public interface RecommendService {
    List<Corpus> getRecommendByUserID(String userid);
}
