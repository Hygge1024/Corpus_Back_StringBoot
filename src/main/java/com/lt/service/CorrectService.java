package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.domain.CorpusDao;
import com.lt.domain.Correct;
import com.lt.domain.CorrectDao;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CorrectService extends IService<Correct> {
    int createCorrect(MultipartFile[] TemplateFiles, MultipartFile[] BatchExerciseFiles,CorrectDao correctDao) throws IOException, InterruptedException;
    int uploadAllExercisesFiles(MultipartFile[] multipartFiles,int corpus_id,int correct_id) throws IOException, InterruptedException;
    int updateExercises(CorrectDao correctDao);
    int delete(int correct_id);
    IPage<Correct> getPage(int currentPage,int pageSize);

}
