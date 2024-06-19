package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.threeLevelDao;
import com.lt.domain.three_level;
import com.lt.domain.two_level;
import com.lt.service.level_threeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 1.根据twoId来查询的接口
 * 2.insert接口
 * 3.update接口
 */
@Service
public class level_threeServiceImpl extends ServiceImpl<threeLevelDao, three_level> implements level_threeService {
    @Autowired
    private threeLevelDao threeDao;
    @Override
    public List<three_level> getall(int two_id) {
        QueryWrapper<three_level> wrapper = new QueryWrapper<>();
        wrapper.eq("two_id",two_id);
        return threeDao.selectList(wrapper);
    }

    @Override
    public int insert(three_level tl) {
        QueryWrapper<three_level> wrapper = new QueryWrapper<>();
        wrapper.eq("three_name",tl.getThreeName());
        three_level t = threeDao.selectOne(wrapper);
        if(t != null){
            return -1;
        }
        return threeDao.insert(tl);
    }

    @Override
    public int update(three_level tl) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("three_id",tl.getThreeId());
        return threeDao.update(tl,queryWrapper);
    }
}
