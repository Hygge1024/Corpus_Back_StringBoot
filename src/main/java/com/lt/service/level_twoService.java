package com.lt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.domain.two_level;

import java.util.List;

public interface level_twoService extends IService<two_level> {
    List<two_level> getall(int exercisesId,int type);
    int insert(two_level two_level);
    int update(two_level two_level);
}
