package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.twoLevelDao;
import com.lt.domain.two_level;
import com.lt.service.level_twoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 1.根据exercises和type 来查询的接口
 * 2.insert接口
 * 3.update接口
 */

@Service
public class level_twoServiceImpl extends ServiceImpl<twoLevelDao, two_level> implements level_twoService {
    @Autowired twoLevelDao twoLevelDao;

    @Override
    public List<two_level> getall(int exercisesId) {
        QueryWrapper<two_level> wrapper = new QueryWrapper<>();
        wrapper.eq("exercises_id",exercisesId);
        return twoLevelDao.selectList(wrapper);
    }

    @Override
    public int insert(two_level two_level) {
        QueryWrapper<two_level> wrapper = new QueryWrapper<>();
        wrapper.eq("exercises_id",two_level.getExercisesId())
                .eq("type",two_level.getTwoId());
        two_level t1 = twoLevelDao.selectOne(wrapper);
        if(t1 != null){
            System.out.println("cele");
            return -1;//已有，不可再添加
        }
        System.out.println(two_level);
        twoLevelDao.insert(two_level);
        return two_level.getTwoId();
    }

    /**
     * 全部参数更新
     * @param two_level 更新类
     * @return 返回操作结果
     */
    @Override
    public int update(two_level two_level) {
        int twoId = two_level.getTwoId();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("two_id",two_level.getTwoId());
        return twoLevelDao.update(two_level,wrapper);
    }
}
