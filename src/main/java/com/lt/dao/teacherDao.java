package com.lt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.teachers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface teacherDao extends BaseMapper<teachers> {
}
