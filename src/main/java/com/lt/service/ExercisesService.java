package com.lt.service;

import com.lt.doadmin.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExercisesService {
    //查询所有的语料资源——不会用到的
    List<Exercises> getAllExercises(String stuId);

    //查询-单个
    Exercises getOneExercises(int eid);

    //查询-分页-根据stuID
    List<Exercises> getPage(int currentPage, int pageSize, String stuId);

    //查询练习-通过cid属性
    List<Exercises> getByCid(int cid);

    //上传学生练习文件（语料资源.mp3 .mp4）
    RcCorpus upload(MultipartFile file);

    //上传完整的语料——语料需要通过url中添加cid属性来查找-在set进去
    int create(ExercisesDao exercises);

    //更新语料——管理（旧版本，接口已经放弃这个方法了）
    int update(ExercisesDao exercisesself);//随时可删除

    //更新语料——管理（教师打分）
    int updateScore(ExerciseScoreDao exerciseScoreDao);

    //删除语料——管理
    int delete(int eid);

    //获取音量+语速分值
    float[] getautoScore(int eid);

    //查询班级学生的所有练习Exercises
    List<Exercises> getExerciseByClass(String stuclas);

}
