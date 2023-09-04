package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.doadmin.download;

public interface DownLoadService extends IService<download> {
    /*
    可以将userid和who放在url最前面
     */
    //查询所有——分页——userid+who
    IPage<download> getAll(int currentPage, int pageSize, String userid, int who);

    //查询——分页——标题——userid+who
    IPage<download> getByTitle(int currentPage, int pageSize, String title, String userid, int who);

    //添加
    int insert(download download);

    //删除
    int delete(int did);

}
