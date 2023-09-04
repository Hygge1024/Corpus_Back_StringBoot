package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.api.ApiKeyRequired;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.doadmin.students;
import com.lt.service.StudentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j   //添加日志记录信息
@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentsService studentsService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*
   查找所有学生
    */
    @GetMapping
    public Result getStuAll() {
        List<students> stuList = studentsService.getStuAll();
        Integer code = stuList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stuList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, stuList);
    }

    /*
    根据sid查找
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
    根据stunumber学号查找
     */
    @GetMapping("/{stunumber}")
    public Result getByStunumber(@PathVariable String stunumber) {
        students stu = studentsService.getByStunumber(stunumber);
        Integer code = stu != null ? Code.GET_OK : Code.GET_ERR;
        String msg = stu != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, stu);
    }

    /*
    分页查询:注意->需要添加拦截器！！！
     */
    @GetMapping("{currentPage}/{pageSize}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<students> page = studentsService.getPage(currentPage, pageSize);
//        如果当前页码值大于总页码值，使用最大页码值
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
    学生注册功能
     */
    @PostMapping(("/register"))
    public Result registe(@RequestBody students stu) {
//        String usernumber = stu.getStunumber();
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("stunumber", usernumber);
//        students user = studentdao.selectOne(wrapper);
//        if (user != null) {
//            return new Result(Code.SAVE_ERR, "用户已存在，请重新输入!", stu);//失败时，返回的是用户输入的信息
//        } else {
//            String hashedPassword = hashPassword(stu.getStupassword());
//            stu.setStupassword(hashedPassword);
//            int flag = studentdao.insert(stu);
//            return new Result(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, "恭喜你,注册成功!", stu);//flag表示影响的行数msg
//        }
        return studentsService.registe(stu);
    }

    /*
       登录检测
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
    删除功能
     */
    @DeleteMapping("/{sid}")
    public Result delete(@PathVariable Integer sid) {
        int flag = studentsService.deleteById(sid);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, flag);
    }

    /*
    更改个人信息:穿过来的密码仅一个，是否相同前端判断
     */
    @PutMapping
    public Result update(@RequestBody students stu) {
        int flag = studentsService.update(stu);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "更改成功" : "更改失败";
        return new Result(code, msg, flag);
    }

    /*
    哈希加密方法
     */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
