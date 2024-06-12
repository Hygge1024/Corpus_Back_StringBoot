package com.lt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.Correct;
import com.lt.domain.Labour;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface labourDao extends BaseMapper<Labour> {
}
