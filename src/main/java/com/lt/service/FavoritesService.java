package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.doadmin.Favorites;
import com.lt.doadmin.students;

import java.util.List;

public interface FavoritesService extends IService<Favorites> {
    /*
    因为收藏是针对个人而言的，因此“查”是针对个人而言，都需要添加userid来区分
    而 增、删、改 在Favorites类中都有相应的userid信息，便不需要单独的userid了
     */
    //查——1,2,3根据身份分页查询 + 还需要添加用户的userid属性（需要根据请求中的用户id来查找个人的所有收藏资源）
    //学生
    IPage<Favorites> getAllStu(int currentPage, int pageSize, String userid);

    //教师
    IPage<Favorites> getAllTea(int currentPage, int pageSize, String userid);

    //管理员
    IPage<Favorites> getAllAdm(int currentPage, int pageSize, String userid);

    //查——根据 “类型” 查找
    IPage<Favorites> getByTag_ids(int who, int currentPage, int pageSize, int tagid, String userid); // 是不是还是需要以 分页 的形式查询

    //查—— “标题title” 搜索查找——模糊查找功能
    IPage<Favorites> getByTitle(int who, int currentPage, int pageSize, String Title, String userid);// 是不是还是需要以 分页 的形式查询

    //添加
    int insert(Favorites favorites);

    //更新
    int update(Favorites favorites);

    //删除
    int delete(int cid);

}
