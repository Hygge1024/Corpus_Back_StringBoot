package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.api.ApiKeyRequired;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Charts;
import com.lt.domain.Exercises;
import com.lt.domain.Task;
import com.lt.domain.students;
import com.lt.service.StudentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // 添加日志记录信息
@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {
    @Autowired
    private StudentsService studentsService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*
     * 查找所有学生
     */
    @GetMapping
    public Result getStuAll() {
        List<students> stuList = studentsService.getStuAll();
        Integer code = stuList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stuList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, stuList);
    }

    /**
     * 获取指定班级的学生总数
     * 
     * @param className 班级名称
     * @return 返回查询结果
     */
    @GetMapping("/classCount/{className}")
    public Result getClassStudentCount(@PathVariable String className) {
        Long count = studentsService.getStudentCountByClassName(className);
        if (count != null) {
            return new Result(Code.GET_OK, "班级学生总数查询成功", count);
        } else {
            return new Result(Code.GET_ERR, "班级学生总数查询失败", null);
        }
    }

    /**
     * 获取指定班级的指定练习情况
     * 
     * @param className  班级名称
     * @param exerciseId 练习id
     * @return 返回查询结果
     */
    @GetMapping("/submissionRatio/{className}/{exerciseId}")
    public Result getSubmissionRatio(@PathVariable String className, @PathVariable int exerciseId) {
        String ratio = studentsService.getSubmissionRatioByClassAndExercise(className, exerciseId);
        return new Result(Code.GET_OK, "查询成功", ratio);
    }

    /*
     * 根据sid查找
     */
    @ApiKeyRequired
    @GetMapping("/bysid/{sid}")
    public Result getBySid(@PathVariable Integer sid) {
        students stu = studentsService.getBySid(sid);
        Integer code = stu != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stu != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, stu);
    }

    /*
     * 根据stunumber学号查找
     */
    @GetMapping("/{stunumber}")
    public Result getByStunumber(@PathVariable String stunumber) {
        students stu = studentsService.getByStunumber(stunumber);
        Integer code = stu != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stu != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, stu);
    }

    /*
     * 分页查询:注意->需要添加拦截器！！！
     */
    @GetMapping("{currentPage}/{pageSize}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<students> page = studentsService.getPage(currentPage, pageSize);
        // 如果当前页码值大于总页码值，使用最大页码值
        if (currentPage > page.getPages()) {
            page = studentsService.getPage((int) page.getPages(), pageSize);
        }
        System.out.println("总页数:" + page.getPages());
        System.out.println("总记录数:" + page.getTotal());
        List<students> stuList = page.getRecords();
        Integer code = stuList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stuList != null ? "查询成功_分页" : "数据查询失败,请重试!";
        return new Result(code, msg, stuList);
    }

    /*
     * 分页查询学生——根据班级
     */
    @GetMapping("/{teaclass}/{currentPage}/{pageSize}")
    public Result getByClassPage(@PathVariable String teaclass, @PathVariable int currentPage,
            @PathVariable int pageSize) {
        IPage page = studentsService.getByClassPage(teaclass, currentPage, pageSize);
        if (currentPage > page.getPages()) {
            page = studentsService.getByClassPage(teaclass, (int) page.getPages(), pageSize);
        }
        List<students> stuList = page.getRecords();
        Integer code = stuList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stuList != null ? "查询成功_分页" : "数据查询失败,请重试!";
        return new Result(code, msg, stuList);
    }

    /*
     * 学生注册功能
     */
    @PostMapping(("/register"))
    public Result registe(@RequestBody students stu) {
        return studentsService.registe(stu);
    }

    /*
     * 登录检测
     */
    @PostMapping("/login")
    public Result loginCheck(@RequestBody students loginUser) {
        String password = loginUser.getStupassword();
        students user = studentsService.loginCheck(loginUser);
        if (user == null) {
            return new Result(Code.LOGIN_ERR, "用户不存在", null);
        }
        if (!passwordEncoder.matches(password, user.getStupassword())) {
            return new Result(Code.LOGIN_ERR, "密码错误", null);
        }
        return new Result(Code.LOGIN_OK, "登录成功", user);
    }

    /*
     * 删除功能
     */
    @DeleteMapping("/{stunumber}")
    public Result delete(@PathVariable String stunumber) {
        int flag = studentsService.deleteByNumber(stunumber);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, flag);
    }

    /*
     * 更改个人信息:传过来的密码仅一个，是否相同前端判断
     */
    @PutMapping
    public Result update(@RequestBody students stu) {
        int flag = studentsService.update(stu);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "更改成功" : "更改失败";
        return new Result(code, msg, flag);
    }

    /**
     * 查询发布了的练习
     *
     * @param stunumber 学号
     * @return 练习
     */
    @GetMapping("/publish/{stunumber}")
    public Result getAllPublish(@PathVariable String stunumber) {
        List<Task> taskList = studentsService.getTaskBySelf(stunumber);
        Integer code = taskList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = taskList != null ? "查询发布练习成功" : "查询发布练习失败失败";
        return new Result(code, msg, taskList);
    }

    /**
     * 学生-成绩波动-标准得分
     *
     * @param stuNumber 学号
     * @return 满足条件的练习
     */
    @GetMapping("/charts/{stuNumber}")
    public Result getCharts(@PathVariable String stuNumber) {
        List<Charts> chartsList = studentsService.getCharts(stuNumber);
        Integer code = chartsList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = chartsList != null ? "查询成功，请开始统计吧！" : "查询失败，请联系开发人员";
        return new Result(code, msg, chartsList);
    }

    /**
     * 学生-题型平均分
     *
     * @param stuNumber 学号
     * @return 满足条件的练习
     */
    @GetMapping("/chartsExercises/{stuNumber}")
    public Result getChartsExercise(@PathVariable String stuNumber) {
        List<Exercises> chartsList = studentsService.getChartsExercise(stuNumber);
        Integer code = chartsList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = chartsList != null ? "查询成功，请开始统计吧！" : "查询失败，请联系开发人员";
        return new Result(code, msg, chartsList);
    }

    /*
     * 哈希加密方法
     */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
