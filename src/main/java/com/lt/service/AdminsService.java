package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.controller.utils.Result;
import com.lt.doadmin.admins;

import java.util.List;

public interface AdminsService extends IService<admins> {
    //查询所有的管理员——不用的
    List<admins> getAdmAll();

    //查询单个管理员信息——个人主页使用
    admins getByAid(Integer aid);

    //通过工号查询个人主页信息
    admins getByAdmnumber(String admnumber);

    //注册功能
    Result registe(admins adm);

    //登录检测功能
    admins loginCheck(admins adm);

    //删除功能
    int deleteById(Integer tid);

    //更新完整的管理员功能
    int update(admins adm);

    //对教师账号状态的审核
    int OkTea(String teanumber);

    int NoTea(String teanumber);
}
