package com.lt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.dao.TaskDao;
import com.lt.dao.teacherDao;
import com.lt.domain.*;
import com.lt.service.ClassesService;
import com.lt.service.CorpusService;
import com.lt.service.TeachersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TeachersServiceImpl extends ServiceImpl<teacherDao, teachers> implements TeachersService {
    @Autowired
    private teacherDao teacherDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private CorpusService corpusService;
    @Autowired
    private ClassesService classesService;

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
    public int publish(Task task) {
        Calendar calendar = Calendar.getInstance();
        Date publishTime = calendar.getTime();//上传时间
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Task::getCorpusid, task.getCorpusid())
                .eq(Task::getTeanumber, task.getTeanumber());
        Task task1 = taskDao.selectOne(wrapper);
        if (task1 != null) {
            return 0;
        }
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


    /*
  哈希加密方法
   */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
