package com.lt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.students;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface studentDao extends BaseMapper<students> {
}
