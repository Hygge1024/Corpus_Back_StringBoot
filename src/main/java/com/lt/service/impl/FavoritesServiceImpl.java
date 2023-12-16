package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.FavoriteDao;
import com.lt.domain.Favorites;
import com.lt.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoriteDao, Favorites> implements FavoritesService {
    @Autowired
    private FavoriteDao favoriteDao;

    @Override
    public IPage<Favorites> getAllStu(int currentPage, int pageSize, String userid) {
        //创建分页对象
        IPage page = new Page(currentPage, pageSize);

        //创建查询条件
        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("who", 1)
                .eq("userid", userid);

        return favoriteDao.selectPage(page, wrapper);
    }

    @Override
    public IPage<Favorites> getAllTea(int currentPage, int pageSize, String userid) {
        //创建分页对象
        IPage page = new Page(currentPage, pageSize);

        //创建查询条件
        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("who", 2)
                .eq("userid", userid);
        return favoriteDao.selectPage(page, wrapper);
    }

    @Override
    public IPage<Favorites> getAllAdm(int currentPage, int pageSize, String userid) {
        //创建分页对象
        IPage page = new Page(currentPage, pageSize);

        //创建查询条件
        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("who", 3)
                .eq("userid", userid);
        return favoriteDao.selectPage(page, wrapper);
    }

    @Override
    public IPage<Favorites> getByTag_ids(int who, int currentPage, int pageSize, int tagid, String userid) {
        //创建分页对象
        IPage page = new Page(currentPage, pageSize);

        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("tagids", tagid)
                .eq("who", who)
                .eq("userid", userid);
        return favoriteDao.selectPage(page, wrapper);
    }

    @Override
    public IPage<Favorites> getByTitle(int who, int currentPage, int pageSize, String Title, String userid) {
        //创建分页对象
        IPage page = new Page(currentPage, pageSize);

        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.like("ctitle", Title)
                .eq("who", who)
                .eq("userid", userid);
        return favoriteDao.selectPage(page, wrapper);
    }

    @Override
    public int insert(Favorites favorites) {
        /*
        注意：需要判断改语料是否已经收藏 了 cid+userid
         */
        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", favorites.getCid())
                .eq("userid", favorites.getUserid())
                .eq("who", favorites.getWho());
        Favorites favorites1 = favoriteDao.selectOne(wrapper);
        if (favorites1 != null) {
            return -1;//表示已经存在，不需要再添加了
        }
        int flag = favoriteDao.insert(favorites);
        return flag;//正常情况，原先没有收藏，现在才收藏
    }

    @Override
    public int update(Favorites favorites) {
        int oldcid = favorites.getFid();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("fid", favorites.getFid());
        int flag = favoriteDao.update(favorites, wrapper);
        return flag;
    }

    @Override
    public int delete(int cid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid", cid);
        return favoriteDao.delete(wrapper);
    }
}
