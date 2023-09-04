package com.lt.service;

import com.lt.doadmin.Corpus;
import com.lt.doadmin.CorpusDao;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CorpusService {
    //查询所有语料——不会用的
    List<Corpus> getAllCorpus();

    //查询单个语料的详细信息
    Corpus getOneCorpus(int cid);

    //分页查询———主要使用部分
    List<Corpus> getPage(int currentPage, int pageSize);

    //根据标签类型查询
    List<Corpus> getByTag_ids(int Tag_ids);

    //综合查询——没进行分页处理（查询的数据不会太多）==>或许是个bug
    List<Corpus> getByFactory(int currentPage, int pageSize, int Direction, int Difficulty, int Type, int tags, String keyWord);

    //上传文件（语料资源.mp3 .mp4）
    Long upload(MultipartFile file);

    //上传完整的语料
    int create(CorpusDao corpusDao);

    //更新语料——管理
    int update(CorpusDao corpusSelf);

    //删除语料——管理
    int delete(int cid);

    //语料库——查询各自上传过的语料资源
    List<Corpus> getSelf(int currentPage, int pageSize, String AuthorID, String Title, String created_at);

}
