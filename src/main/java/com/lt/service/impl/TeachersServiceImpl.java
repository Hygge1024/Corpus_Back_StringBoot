package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.CorpusController;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.dao.TaskDao;
import com.lt.dao.teacherDao;
import com.lt.domain.*;
import com.lt.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TeachersServiceImpl extends ServiceImpl<teacherDao, teachers> implements TeachersService {
    private static String Url;
    @Autowired
    private teacherDao teacherDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private com.lt.dao.studentDao studentDao;
    @Autowired
    private CorpusService corpusService;
    @Autowired
    private ClassesService classesService;
    @Autowired
    private ExercisesService exercisesService;
    @Autowired
    private BaiduService baiduService;

    @Value("${strapi.url}")
    public void setUrl(String url) {
        TeachersServiceImpl.Url = url;
    }

    @Override
    public List<teachers> getTeaAll() {
        return teacherDao.selectList(null);
    }

    @Override
    public teachers getByTid(Integer tid) {
        return teacherDao.selectById(tid);
    }

    @Override
    public teachers getByTeanumber(String teanumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", teanumber);
        return teacherDao.selectOne(wrapper);
    }

    @Override
    public IPage<teachers> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        teacherDao.selectPage(page, null);
        return page;
    }

    @Override
    public Result registe(teachers tea) {
        String usernumber = tea.getTnumber();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", usernumber);
        teachers user = teacherDao.selectOne(wrapper);
        if (null != user) {
            return new Result(Code.SAVE_ERR, "用户已存在，请重新输入!", tea);
        } else {
            String hashedPassword = hashPassword(tea.getTeapassword());
            tea.setTeapassword(hashedPassword);
            int flag = teacherDao.insert(tea);
            return new Result(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, "恭喜你,注册成功!", tea);
        }
    }

    @Override
    public teachers loginCheck(teachers tea) {
        String usernumber = tea.getTnumber();
        String password = tea.getTeapassword();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("tnumber", usernumber);
        teachers user = teacherDao.selectOne(wrapper);
        return user;
    }

    @Override
    public int deleteById(Integer tid) {
        return teacherDao.deleteById(tid);
    }

    @Override
    public int update(teachers tea) {
//        String hashedPassword = hashPassword(tea.getTeapassword());//对密码加密
//        tea.setTeapassword(hashedPassword);
        QueryWrapper wrapper = new QueryWrapper();
        String teanumber = tea.getTnumber();
        tea.setTeastate(1);
        wrapper.eq("tnumber", tea.getTnumber().toString());
        int flag = teacherDao.update(tea, wrapper);
        return flag;
    }

    @Override
    public int publish(MultipartFile file, Task task) {
        Calendar calendar = Calendar.getInstance();
        Date publishTime = calendar.getTime();//上传时间

        //上传音频、视频文件
        RcCorpus rcCorpus = corpusService.upload(file);
        rcCorpus.setFileUrl(Url.replaceAll("/$", "")+rcCorpus.getFileUrl());
        log.info("模板上传后的fid：" + rcCorpus.getFileId());
        log.info("模板上传后的URL：" + rcCorpus.getFileUrl());
        task.setFileurl(rcCorpus.getFileUrl());

        //判断是否已上传该练习
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Task::getCorpusid, task.getCorpusid())
                .eq(Task::getTeanumber, task.getTeanumber());
        Task task1 = taskDao.selectOne(wrapper);//获取练习
        if (task1 != null) {//练习非空——重新发布
            //对Task表更新(state、classname、taskname、fileurl、keywords、text、audio、duration_percentage)
            task1.setState(1);//更改state值，确保处于发布状态
            task1.setClassname(task.getClassname());
            task1.setTaskname(task.getTaskname());
            task1.setFileurl(task.getFileurl());
            task1.setKeywords(task.getKeywords());
            task1.setText(task.getText());
            task1.setAudio(task.getAudio());
            task1.setDuration_percentage(task.getDuration_percentage());
            log.info("更新后的task1："+task1);
            QueryWrapper<Task> wrapper2 = new QueryWrapper<>();
            wrapper2.lambda()
                    .eq(Task::getTsid, task1.getTsid());
            taskDao.update(task1, wrapper2);
            //对Corpus语料状态更新
            Corpus corpus = corpusService.getOneCorpus(task1.getCorpusid());
            CotpusDao2 corpusDao = new CotpusDao2();
            corpusDao.setId(corpus.getId());
            corpusDao.setPublished(1);//设置为更新状态——针对教师
            corpusService.update2(corpusDao);
            return 0;
        } else {//第一次发布
            task.setState(1);//针对学生更新
            task.setPublishtime(publishTime);
            //对语料状态更新
            Corpus corpus = corpusService.getOneCorpus(task.getCorpusid());
            CotpusDao2 corpusDao = new CotpusDao2();
            corpusDao.setId(corpus.getId());
            corpusDao.setPublished(1);//设置为更新状态——针对教师
            corpusService.update2(corpusDao);
            return taskDao.insert(task);
        }

    }

    /**
     * 取消发布
     *
     * @param cid 语料id
     * @return 状态
     */
    @Override
    public int notpublish(int cid, String teanumber) {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Task::getCorpusid, cid)
                .eq(Task::getTeanumber, teanumber);
        Task task = taskDao.selectOne(wrapper);
        task.setState(2);//取消发布——针对学生
        //更新corpus中的published属性
        Corpus corpus = corpusService.getOneCorpus(cid);
//        log.info("corpus：" + corpus);
        CotpusDao2 corpusDao = new CotpusDao2();
        corpusDao.setId(corpus.getId());
        corpusDao.setPublished(2);//设置为取消更新状态——针对教师
//        log.info("corpusDao：" + corpusDao);
        corpusService.update2(corpusDao);

        QueryWrapper<Task> wrapper2 = new QueryWrapper();
        wrapper2.lambda()
                .eq(Task::getTsid, task.getTsid());
        return taskDao.update(task, wrapper2);
    }

    /**
     * 根据班级获取 练习情况
     *
     * @param className 班级名称
     * @return 返回班级的练习号+学生练习
     */
    public List<Charts> getCharts(String className) {
        //1.通过班级名称查询班级学生
        QueryWrapper<students> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(students::getStuclass, className);
        List<students> studentsList = studentDao.selectList(wrapper);
        //2.学号查询练习
        List<Exercises> exercisesList = new ArrayList<>();
        for (students students : studentsList) {
            exercisesList.addAll(exercisesService.getAllExercises(students.getStunumber()));
        }
        //3.练习中的CorpusID组合班级名称得到练习的编号
        List<Charts> chartsList = new ArrayList<>();
        for (Exercises exercises : exercisesList) {
            //过滤批改过的
            if (exercises.getScore() >= 0) {
                //4.将练习id及exercises对象 生成Charts对象
                int corpusID = exercises.getCorpus().getId();
                QueryWrapper<Task> wrapper1 = new QueryWrapper<>();
                wrapper1.lambda()
                        .eq(Task::getClassname, className)
                        .eq(Task::getCorpusid, corpusID);
                Task task = taskDao.selectOne(wrapper1);
                if (null != task) {
                    int taskID = task.getTsid();
                    Charts charts = new Charts();
                    charts.setTaskid(taskID);
                    charts.setExercises(exercises);
                    chartsList.add(charts);
                }
            }
        }
        return chartsList;
    }

    @Override
    public String getWenxin_Value(int eid, String WenXinAPI, String WenXinSecurity) throws IOException {
        Exercises exercises = exercisesService.getOneExercises(eid);
        String contentA = exercises.getIdentifyText();//学生的文本
        String contentB = exercises.getCorpus().getOriginaltext();//语料原文

        return baiduService.WenXin_Value(contentA.replaceAll("[\\r\\n]", ""), contentB.replaceAll("[\\r\\n]", ""), WenXinAPI, WenXinSecurity);
    }


    /*
  哈希加密方法
   */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
