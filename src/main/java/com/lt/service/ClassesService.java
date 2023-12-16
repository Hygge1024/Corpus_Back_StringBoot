package com.lt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.domain.Classes;

import java.util.List;

public interface ClassesService extends IService<Classes> {

    //    查询（tnumber）
    List<Classes> getAllClass();

    List<Classes> getBySelf(String tnumber);

    //    添加（tnumber，cname=》Classes对象） 需要判断是否已经存在
    int addClss(Classes classes);

    //    删除（tnumber，cid（编号））（不建议用户删除班级，学生如果遇到班级被删除，需要显示空白）
    int deleteByCid(String cid);

    //    更新（tnumber，cid（唯一属性），newname替换cname值）
    int update(Classes classes);

}
