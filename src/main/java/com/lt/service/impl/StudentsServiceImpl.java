package com.lt.service.impl;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.dao.TaskDao;
import com.lt.dao.studentDao;
import com.lt.domain.*;
import com.lt.service.CorpusService;
import com.lt.service.ExercisesService;
import com.lt.service.StudentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StudentsServiceImpl extends ServiceImpl<studentDao, students> implements StudentsService {
    @Autowired
    private studentDao studentDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private CorpusService corpusService;
    @Autowired
    private ExercisesService exercisesService;

    @Override
    public List<students> getStuAll() {
        return studentDao.selectList(null);
    }

    @Override
    public students getBySid(Integer sid) {
        return studentDao.selectById(sid);
    }

    @Override
    public students getByStunumber(String stunumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", stunumber);
        return studentDao.selectOne(wrapper);
    }

    @Override
    public IPage<students> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        studentDao.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<students> getByClassPage(String cname, int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stuclass", cname);

        return studentDao.selectPage(page, wrapper);
    }

    @Override
    public Result registe(students stu) {
        log.info("学生用户开始了注册：" + stu.getStunumber());
        String usernumber = stu.getStunumber();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", usernumber);
        students user = studentDao.selectOne(wrapper);
        if (user != null) {
            log.info("学生用户的注册信息已存在，请重新注册");
            return new Result(Code.SAVE_ERR, "用户已存在，请重新输入!", stu);// 失败时，返回的是用户输入的信息
        } else {
            String hashedPassword = hashPassword(stu.getStupassword());
            stu.setStupassword(hashedPassword);
            int flag = studentDao.insert(stu);
            return new Result(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, "恭喜你,注册成功!", stu);// flag表示影响的行数msg
        }
    }

    @Override
    public students loginCheck(students stu) {
        String usernumber = stu.getStunumber();
        String password = stu.getStupassword();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", usernumber);
        students user = studentDao.selectOne(wrapper);
        return user;
    }

    @Override
    public int deleteByNumber(String stunumber) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stunumber", stunumber);
        return studentDao.delete(wrapper);
    }

    @Override
    public int update(students stu) {
        // String hashedPassword = hashPassword(stu.getStupassword());//对新密码进行加密
        // stu.setStupassword(hashedPassword);
        QueryWrapper wrapper = new QueryWrapper();
        String stunumber = stu.getStunumber();
        log.info("密码为：" + stu.getStupassword());
        wrapper.eq("stunumber", stu.getStunumber().toString());
        int flag = studentDao.update(stu, wrapper);
        return flag;
    }

    @Override
    public int updateClass(String oldClassName, String newClassName) {
        System.out.println(oldClassName + "   " + newClassName);
        if ("delete".equals(newClassName)) {
            newClassName = " ";
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stuclass", oldClassName);
        List<students> studentsList = studentDao.selectList(wrapper);
        // System.out.println("成功查询所有的班级信息");
        wrapper.clear();
        for (students stu : studentsList) {
            // System.out.println(stu);
            stu.setStuclass(newClassName);
            wrapper.eq("stunumber", stu.getStunumber().toString());
            studentDao.update(stu, wrapper);
            wrapper.clear();
        }
        return studentsList.size();
    }

    /**
     * 获取所属班级的练习
     *
     * @param stunumber 学生学号
     * @return 返回所有待练习
     */
    @Override
    public List<Task> getTaskBySelf(String stunumber) {
        students stu = this.getByStunumber(stunumber);
        String className = stu.getStuclass();
        // 然后开始查询《Task表》中等于className的
        QueryWrapper<Task> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(Task::getClassname, className)
                .eq(Task::getState, 1);
        List<Task> taskList = taskDao.selectList(wrapper);// 查询了所有满足条件的的练习表
        for (Task task : taskList) {
            task.setCorpus(corpusService.getOneCorpus(task.getCorpusid()));
        }
        return taskList;
    }

    /**
     * 查询学生的练习-可视化 成绩、标准得分
     *
     * @param stunumber 学号
     * @return 满足条件的练习
     */
    @Override
    public List<Charts> getCharts(String stunumber) {
        // 1.查询所有的练习
        List<Exercises> exercisesList = exercisesService.getAllExercises(stunumber);
        // 2.过滤练习-被批改了的
        // 获取班级
        QueryWrapper<students> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(students::getStunumber, stunumber);
        String className = studentDao.selectOne(wrapper).getStuclass();
        List<Charts> chartsList = new ArrayList<>();
        for (Exercises exercises : exercisesList) {
            if (exercises.getScore() >= 0) {
                // 3.班级信息+corpusID 得到 当前练习的 练习taskid
                int corpusID = exercises.getCorpus().getId();
                QueryWrapper<Task> wrapper1 = new QueryWrapper<>();
                wrapper1.lambda()
                        .eq(Task::getClassname, className)
                        .eq(Task::getCorpusid, corpusID);
                Task task = taskDao.selectOne(wrapper1);
                // 4.将上面得到的taskID + exercise 组合成charts
                if (task != null) {
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

    /**
     * 学生练习-可视化（对标签tag_ids进行了补充）
     *
     * @param stunumber 学号
     * @return 满足条件的练习
     */
    @Override
    public List<Exercises> getChartsExercise(String stunumber) {
        // 1.查询学生的所有练习
        List<Exercises> exercisesList = exercisesService.getAllExercises(stunumber);
        List<Exercises> exercisesList2 = new ArrayList<>();
        // 2.对练习进行 tag的补充
        for (Exercises exercises : exercisesList) {
            // 3.过滤-被批改
            if (exercises.getScore() >= 0) {
                Corpus corpus = corpusService.getOneCorpus(exercises.getCorpus().getId());
                exercises.setCorpus(corpus); // 更新Tag_ids标签
                exercisesList2.add(exercises);
            }
        }
        return exercisesList2;
    }

    /*
     * 哈希加密方法
     */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public Map<String, Integer> getStudentCountByClass() {
        // 使用MyBatis Plus的查询构造器进行分组和计数
        QueryWrapper<students> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("teaclass", "COUNT(*) as student_count").groupBy("teaclass");
        List<Map<String, Object>> results = listMaps(queryWrapper);

        // 将结果转换为Map<String, Integer>
        Map<String, Integer> classCounts = new HashMap<>();
        for (Map<String, Object> result : results) {
            classCounts.put((String) result.get("teaclass"), ((Long) result.get("student_count")).intValue());
        }
        return classCounts;
    }

    @Override
    public Long getStudentCountByClassName(String className) {
        QueryWrapper<students> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stuclass", className);
        return studentDao.selectCount(queryWrapper);
    }

    @Override
    public String getSubmissionRatioByClassAndExercise(String className, int exerciseId) {
        // 获取班级总学生数
        Long totalStudents = this.getStudentCountByClassName(className);

        // 获取提交该练习的学生数
        List<Exercises> submittedExercises = exercisesService.getExerciseSubmissionsByClassAndExercise(className,
                exerciseId);
        int submittedCount = submittedExercises.size();

        // 返回比例
        return submittedCount + ":" + totalStudents;
    }

}
