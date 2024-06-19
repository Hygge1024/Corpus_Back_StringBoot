package com.lt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.domain.three_level;
import com.lt.domain.two_level;

import java.util.List;

public interface level_threeService extends IService<three_level> {
    List<three_level> getall(int two_id);
    int insert(three_level tl);
    int update(three_level tl);
}
