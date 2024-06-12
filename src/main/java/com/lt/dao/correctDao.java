package com.lt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.Classes;
import com.lt.domain.Correct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface correctDao extends BaseMapper<Correct> {
}
