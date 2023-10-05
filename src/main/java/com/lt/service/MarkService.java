package com.lt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.doadmin.Mark;
import com.lt.doadmin.MarkUpDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MarkService extends IService<Mark> {
    //查询所有的评论
    List<Mark> getAll(int eid, String stunumber);

    //添加评论（教师添加）
    int addMark(Mark mark);

    //更新评论
    int updateMark(MarkUpDao markUpDao);

    //删除评论
    int deleteMark(int mid);
}
