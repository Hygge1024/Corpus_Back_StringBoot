package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.markDao;
import com.lt.doadmin.Mark;
import com.lt.doadmin.MarkUpDao;
import com.lt.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkServiceImple extends ServiceImpl<markDao, Mark> implements MarkService {
    @Autowired
    private markDao markDao;

    @Override
    public List<Mark> getAll(int eid, String stunumber) {
        QueryWrapper<Mark> wrapper = new QueryWrapper<>();
        wrapper.eq("eid", eid)
                .eq("stunumber", stunumber);
        return markDao.selectList(wrapper);
    }

    @Override
    public int addMark(Mark mark) {
        return markDao.insert(mark);
    }

    @Override
    public int updateMark(MarkUpDao markUpDao) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("mid", markUpDao.getMid());
        Mark mark = markDao.selectOne(wrapper);
        mark.setMtext(markUpDao.getMtext());
        return markDao.update(mark, wrapper);
    }

    @Override
    public int deleteMark(int mid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("mid", mid);
        return markDao.delete(wrapper);
    }
}
