package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.downloadDao;
import com.lt.domain.download;
import com.lt.service.DownLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownLoadServiceImpl extends ServiceImpl<downloadDao, download> implements DownLoadService {
    @Autowired
    private downloadDao downloadDao;

    @Override
    public IPage<download> getAll(int currentPage, int pageSize, String userid, int who) {
        IPage page = new Page(currentPage, pageSize);

        QueryWrapper<download> wrapper = new QueryWrapper<>();
        wrapper.eq("who", who)
                .eq("userid", userid);
        return downloadDao.selectPage(page, wrapper);
    }

    @Override
    public IPage<download> getByTitle(int currentPage, int pageSize, String title, String userid, int who) {
        IPage page = new Page(currentPage, pageSize);

        QueryWrapper<download> wrapper = new QueryWrapper<>();
        wrapper.eq("who", who)
                .eq("userid", userid)
                .like("title", title);
        return downloadDao.selectPage(page, wrapper);
    }

    @Override
    public int insert(download download) {
        QueryWrapper<download> wrapper = new QueryWrapper<>();
        wrapper.eq("did", download.getDid())
                .eq("userid", download.getUserid())
                .eq("who", download.getWho());
        download download1 = downloadDao.selectOne(wrapper);
        if (download1 != null) {
            return -1;//表示已存在，不需要再添加了
        }
        int flag = downloadDao.insert(download);
        return flag;
    }

    @Override
    public int delete(int did) {
        return downloadDao.deleteById(did);
    }

}
