package com.lt.service;

import com.lt.domain.Corpus;
import com.lt.domain.CorpusDao;
import com.lt.domain.CotpusDao2;
import com.lt.domain.RcCorpus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    //根据教师id查询所有语料
    List<Corpus> getByTnumber(String tnumber);

    //综合查询——没进行分页处理（查询的数据不会太多）==>或许是个bug
    List<Corpus> getByFactory(int currentPage, int pageSize, int Direction, int Difficulty, int Type, int tags, String keyWord,String AuthorID);

    //上传文件（语料资源.mp3 .mp4）
    RcCorpus upload(MultipartFile file);

    //上传图片 （PNG、JPG）
    RcCorpus uploadPic(MultipartFile file);

    //上传完整的语料
    int create(CorpusDao corpusDao);

    //更新语料——管理
    int update(CorpusDao corpusSelf);

    int update2(CotpusDao2 corpusSelf);

    //删除语料——管理
    int delete(int cid);

    //语料库——查询各自上传过的语料资源
    List<Corpus> getSelf(int currentPage, int pageSize, String AuthorID, String Title, String created_at);

    //发送知识图谱信息——不会用到的
    Object readJson(String filePath, Class<?> valueType) throws IOException;

}
