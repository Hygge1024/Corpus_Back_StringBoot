package com.lt.service;

import com.lt.doadmin.CorpusDao;
import com.lt.doadmin.Exercises;
import com.lt.doadmin.ExercisesDao;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExercisesService {
    //查询所有的语料资源——不会用到的
    List<Exercises> getAllExercises(String stuId);

    //查询-单个
    Exercises getOneExercises(int eid);

    //查询-分页-根据stuID
    List<Exercises> getPage(int currentPage, int pageSize, String stuId);

    //上传文件（语料资源.mp3 .mp4）
    Long upload(MultipartFile file);

    //上传完整的语料——语料需要通过url中添加cid属性来查找-在set进去
    int create(ExercisesDao exercises);

    //更新语料——管理
    int update(ExercisesDao exercisesself);

    //删除语料——管理
    int delete(int eid);

}
