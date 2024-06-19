package com.lt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.threeLevelDao;
import com.lt.domain.three_level;
import com.lt.service.level_threeService;
import org.springframework.stereotype.Service;

/**
 * 1.根据twoId来查询的接口
 * 2.insert接口
 * 3.update接口
 */
@Service
public class level_threeServiceImpl extends ServiceImpl<threeLevelDao, three_level> implements level_threeService {
}
