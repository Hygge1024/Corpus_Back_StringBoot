package com.lt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.doadmin.Favorites;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteDao extends BaseMapper<Favorites> {
}
