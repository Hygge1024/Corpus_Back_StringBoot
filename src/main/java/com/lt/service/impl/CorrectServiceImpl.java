package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.ExerciseController;
import com.lt.dao.correctDao;
import com.lt.domain.*;
import com.lt.service.BaiduService;
import com.lt.service.CorpusService;
import com.lt.service.CorrectService;
import com.lt.service.ExercisesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 试题批改Service，
 * 1.“导入数据”:用于创建试题批改任务（处理上传过程中的原始corpus、标准exercises,correct_type = 2）
 * 2.批量上传学生作答（使用exercises接口，correct_type = 0）
 * 3.查询所有的correct类（需要新建一个展示 “类” -> 根据前端展示页面选择字段）
 * 4.删除功能（假删除，更改当前correct.state 值）
 * 5.“编辑”-信息（暂时前端没明确要更新什么信息）
 * 6.人工批改-查询界面（查询当前教师 待批改的练习）
 *
 * 还有个任务：考试批改接口需要新写（细分维度接口，需要新建一个维度表，不能在exercises中再新添加属性），根据前端界面写（暂时没写具体的逻辑思路1）
 */
@Service
@Slf4j
public class CorrectServiceImpl extends ServiceImpl<correctDao, Correct> implements CorrectService {
    @Autowired
    private CorpusService corpusService;
    @Autowired
    private ExercisesService exercisesService;
    @Autowired
    private correctDao CorrectDao;
    @Autowired
    private BaiduService baiduService;
    private static String BaiDuAPI_KEY;
    private static String BaiDuSECRET_KEY;
    private static String Url;

    @Value("${baidu.BaiDuAPI_KEY}")
    public void setBaiDuAPI_KEY(String BaiDuAPI_KEY) {
        CorrectServiceImpl.BaiDuAPI_KEY = BaiDuAPI_KEY;
    }

    @Value("${baidu.BaiDuSECRET_KEY}")
    public void setBaiDuSECRET_KEY(String BaiDuSECRET_KEY) {
        CorrectServiceImpl.BaiDuSECRET_KEY = BaiDuSECRET_KEY;
    }

    @Value("${strapi.url}")
    public void setUrl(String url) {
        CorrectServiceImpl.Url = url;
    }

    /**
     * 创建批改项目
     * @param TemplateFiles 语料原文文件
     * @param BatchExerciseFiles 标准练习文件
     * @param correctDao 两个文件对应的相关信息
     * @return 返回结果判断
     * @throws IOException IO异常
     * @throws InterruptedException 异常
     */
    @Override
    public int createCorrect(MultipartFile[] TemplateFiles,MultipartFile[] BatchExerciseFiles, CorrectDao correctDao) throws IOException, InterruptedException {
        MultipartFile corpusFile = TemplateFiles[0];//语料
        MultipartFile exerciseFile = TemplateFiles[1];//标准练习

        //1.先上传corpus
        //先上传语料原文音频
        log.info("1.1开始上传语料文件");
        RcCorpus rcCorpus = new RcCorpus();
        rcCorpus = corpusService.upload(corpusFile);
        log.info("1.2上传完毕，返回的信息为："+rcCorpus);
        //开始上传完整的Corpus
        CorpusDao corpusDao = new CorpusDao();
        corpusDao.setFile(rcCorpus.getFileId());
        corpusDao.setTitle(correctDao.getTitle());
        corpusDao.setType(correctDao.getType());
        corpusDao.setDirection(correctDao.getDirection());
        corpusDao.setTag_ids(correctDao.getTag_ids());
        corpusDao.setOriginaltext(correctDao.getOriginaltext());
        corpusDao.setState(1);
        corpusDao.setPublished(1);
        corpusDao.setAuthorID(correctDao.getUserID());//上传者工号
        //下面三个是前端没设计的属性（为了套用接口，设置默认值）

        corpusDao.setDifficulty(1);//前端未设计
        corpusDao.setIntroduction("发布于批改项目中的语料corpus");//固定描述
//        log.info("corpusDao:"+corpusDao);

        int corpus_id = corpusService.create(corpusDao);
        if(corpus_id == 0){
            log.error("corupus_id为零");
            return 0;
        }
        log.info("1.3完成语料的上传，返回corpus_id："+corpus_id);

        //2.新增correct表
        Correct correct = new Correct();
        correct.setCorrectId(corpus_id);
        correct.setState(1);
        CorrectDao.insert(correct);
        int correct_id = correct.getCorrectId();
        if(correct_id == 0){
            log.error("correct_id为零");
            return 0;
        }
        log.info("2.1新增correct完成，correct_id："+correct_id);
        //3.上传标准exercises
        //先上传练习的音频资源
        log.info("3.1开始上传练习文件");
        RcCorpus rcExercise = exercisesService.upload(exerciseFile);
        log.info("3.2上传完毕，返回的信息为："+rcExercise);
        ExercisesDao exercisesDao = new ExercisesDao();
        exercisesDao.setStuFile(rcExercise.getFileId());
        exercisesDao.setCorpus(correct_id);
        exercisesDao.setIdentifyText(correctDao.getIdentifyText());
        exercisesDao.setCorrecttype(1);
        exercisesDao.setCorrectid(correct_id);
        //下面是未设置的属性
        exercisesDao.setStuID(correctDao.getUserID());
        int exercises_id = exercisesService.create(exercisesDao);
        if(exercises_id == 0){
            log.error("exercises_id为零");
            return 0;
        }
        log.info("3.3完成标准练习的上传，exercises_id:" + exercises_id);

        int exercises = this.uploadAllExercisesFiles(BatchExerciseFiles,corpus_id,correct_id);
        return 1;
    }

    /**
     * 上传批量练习
     * @param multipartFiles 练习文件
     * @param corpus_id 当前的语料ID
     * @param correct_id 当前的项目id
     * @return 返回操作结果
     * @throws IOException IO异常
     * @throws InterruptedException 异常
     */
    @Override
    public int uploadAllExercisesFiles(MultipartFile[] multipartFiles,int corpus_id,int correct_id) throws IOException, InterruptedException {
        ExercisesDao exercisesDao = new ExercisesDao();
        log.info("4.1开始批量上传学生练习");
        int count = 1;
        for(MultipartFile multipartFile : multipartFiles){
            log.info("4."+count+".1开始上传练习文件");
            String fileName = multipartFile.getOriginalFilename();
            if (fileName == null || !fileName.contains("_")) {
                log.error("文件名无效或不包含下划线：" + fileName);
                continue;//文件名不规范的就跳过
            }
            String stuID = fileName.split("_")[0];

            //上传语料资源
            RcCorpus rcExercise = exercisesService.upload(multipartFile);
            log.info("4."+count+".2上传文件完毕，返回信息:"+rcExercise);
            exercisesDao.setStuFile(rcExercise.getFileId());
            exercisesDao.setCorpus(correct_id);
            exercisesDao.setCorrecttype(0);
            /*
            这里需要思考性能问题！
             */
//            exercisesDao.setIdentifyText("自动识别，现在还是后续进行");
            String result = baiduService.Toriginaltext(Url.substring(0, Url.length() - 1) + rcExercise.getFileUrl(),
                    corpusService.getOneCorpus(exercisesDao.getCorpus()).getDirection(), BaiDuAPI_KEY, BaiDuSECRET_KEY);
            exercisesDao.setIdentifyText(result);

            exercisesDao.setCorrectid(correct_id);
            exercisesDao.setStuID(stuID);
            int exercises_id = exercisesService.create(exercisesDao);
            if(exercises_id == 0){
                log.error("exercises_id为零");
                return 0;
            }
            log.info("4."+count+".3成标准练习的上传，exercises_id:" + exercises_id);
            count++;
        }
        return 1;
    }

    /**
     * 删除操作（伪删除）
     * @param correct_id 批改项目ID
     * @return 返回操作结果
     */
    @Override
    public int delete(int correct_id) {

        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("correct_id",correct_id);
        wrapper.set("state",0);
        return CorrectDao.update(null,wrapper);
    }

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return page对象
     */
    @Override
    public IPage<Correct> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",1);
        return CorrectDao.selectPage(page,wrapper);
    }


}
